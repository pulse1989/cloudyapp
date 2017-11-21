package za.co.kernelpanic.cloudy.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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
    private static final int CHECK_PLAY_SERVICES = 9000;
    private static final int FINE_LOCATION_REQUEST_CODE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //we need to ask for permissions on google play services, GPS
        checkPlayServices();

        if(savedInstanceState == null) {

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
            checkPermissions();
            return true;
        }


    }

    private void checkPermissions() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
        }

    }

    //handle our permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode) {

            case FINE_LOCATION_REQUEST_CODE: {

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(LOG_TAG, "We have permissions! proceeding...");

                } else {

                    Log.w(LOG_TAG, "We've been denied permissions, Therefor we cannot give accurate weather information");
                    displayWarningDialog(); //do this before killing the app
                    finish(); // we close the app because we can no longer function.
                }
            }
        }
    }



    private void displayWarningDialog() {
        //TODO - Complete this dialog box warning the user we need both permissions to continue
    }


}
