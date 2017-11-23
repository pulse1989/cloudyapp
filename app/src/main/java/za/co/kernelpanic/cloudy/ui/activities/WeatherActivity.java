package za.co.kernelpanic.cloudy.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import za.co.kernelpanic.cloudy.R;
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherFragment;

public class WeatherActivity extends AppCompatActivity  implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> androidInjector; //we need to inject our android fragment instances into the activity
    private static final String LOG_TAG = WeatherActivity.class.getSimpleName();
    @Inject LocationRequest locationRequest;

    private static final int CHECK_PLAY_SERVICES = 9000;
    private static final int FINE_LOCATION_REQUEST_CODE = 8;
    private static final int CHECK_DEVICE_SETTINGS = 8000;
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private CoordinatorLayout weatherActivityLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         /*
          * we need this here before calling onCreate otherwise our fragment attachments can end up failing
          * as there is a possibility the fragment can get attached while the activity calles super().
          * Before that happens, our activity has to be injected! otherwise any fragments that rely on this activity can crash
          */
        AndroidInjection.inject(this);

        //We then continue as normal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherActivityLayout = findViewById(R.id.mainLayout);
        settingsClient = LocationServices.getSettingsClient(this);
        //we need to ask for permissions on google play services, GPS

        /*
         * Only if we haven't got anything in onSaveInstanceState and we've confirmed checkplay services is here.
         * we can go ahead and attach the fragment
         */
        if(savedInstanceState == null && checkPlayServices() ) {

            loadFragment();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return androidInjector;
    }

    private void loadFragment() {

        WeatherFragment weatherFragment = new WeatherFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().add(R.id.mainLayout, weatherFragment, "weather");
        transaction.commit();
    }


    /*
     * As we are using Google Play Services new Location API for this app, we have to ensure all dependencies are met.
     * This includes using the correct version of play services that is on the device. We take care of that here.
     */
    private boolean checkPlayServices(){

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {

            if(apiAvailability.isUserResolvableError(result)) {
                apiAvailability.getErrorDialog(this, result,  CHECK_PLAY_SERVICES).show();
            }

            Log.e(LOG_TAG, "Play Services package is missing, unable to continue");

            return false;

        } else {

            Log.d(LOG_TAG, "We have play services installed. continuing...");

            return true;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!checkPermissions()) {

            requestPermissions();
        }
    }

    /*
         * if this is the first time we're launching, let's ask the user for Location permissions before implementing this api. We need to do that here
         * before we can subscribe to location updates. We'll include a decent rationale as well to let them know this is so we get the correct weather
         * from their location
         */
    private boolean checkPermissions() {

        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;

    }


    private void requestPermissions() {

        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(shouldProvideRationale) {

            Log.i(LOG_TAG, "showing rational to the user for more information");

            Snackbar.make(weatherActivityLayout, R.string.location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok,
                    v -> ActivityCompat.requestPermissions(WeatherActivity.this, new String[]
                           {Manifest.permission.ACCESS_FINE_LOCATION},
                           FINE_LOCATION_REQUEST_CODE))
                    .show();

        } else {

            Log.i(LOG_TAG, "Requesting user permission");
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
        }
    }

    /*
     * We handle our permission results here to ensure we have the correct results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode) {

            case FINE_LOCATION_REQUEST_CODE:

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(LOG_TAG, "We have permissions! proceeding...");
                    buildLocationSettingsRequest();
                    checkDeviceSettings();


                } else {

                    Log.w(LOG_TAG, "We've been denied permissions, Therefor we cannot give accurate weather information");

                }
                break;

        }
    }

    private void buildLocationSettingsRequest(){
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }


    /*
     * here, we check if the device settings for the location are actually on. We may have permission, but the device needs
     * to accommodate this. We can make sure we check the settings here.
     * If the location settings aren't what we need them to be (HIGH accuracy) we first try to execute a PendingIntent to launch
     * the settings dialogue in order to resolve this. If the PendingIntent fails, the user has to resolve the location settings on their own.
     */

    private void checkDeviceSettings() {

        settingsClient.checkLocationSettings(locationSettingsRequest)

                .addOnSuccessListener(this, locationSettingsResponse -> {

                    Log.i(LOG_TAG, "User has correct setting set.");
                })
                .addOnFailureListener(this, e -> {

                    int statusCode = ((ApiException)e).getStatusCode();

                    switch (statusCode) {

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            Log.w(LOG_TAG, "We don't have the required settings. trying to resolve..");

                            try {
                                     ResolvableApiException rae = (ResolvableApiException)e;
                                     rae.startResolutionForResult(WeatherActivity.this, CHECK_DEVICE_SETTINGS);

                            } catch (IntentSender.SendIntentException ise) {

                                Log.w(LOG_TAG, "Unable to execute PendingIntent");
                            }

                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            String errorMessage = "Unable to solve settings via this App. Please resolve in location settings";
                            Log.w(LOG_TAG, errorMessage);
                            Toast.makeText(WeatherActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                            break;

                    }
                });

    }

    /*
     * This will be done when the user has returned from the settings dialog.
     * RESULT = OK -> the user has made the change and we can continue with loading up the app.
     * otherwise, they have to resolve this manually
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case CHECK_DEVICE_SETTINGS:

                switch (resultCode){

                    case Activity.RESULT_OK:
                        Log.i(LOG_TAG, "The user has agreed to make the settings change");
                         break;

                         case Activity.RESULT_CANCELED:
                         Log.w(LOG_TAG, "The user cancelled request, they'll have to fix it manually");

                         break;
                }

                break;

        }

    }
}
