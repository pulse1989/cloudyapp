package za.co.kernelpanic.cloudy.utils;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;


/**
 * This class is responsible for getting the device location as requested by our repository.
 * The ViewModel would then use this data to send the remote request. The UI  will be loading during this process
 */

public class LocationUtils {

    private static final String  LOG_TAG = LocationUtils.class.getSimpleName();

    /*
     * We have access to the user's location via this API. It requires google play services to be installed on the device.
     */
    private FusedLocationProviderClient fusedLocationClient;

    // Our application context - provided by dagger
    private Context context;

    /*
     *  We declare the location Mutable LiveData here as this is responsible for updating our weather request to any observers that are listening.
     *  Because we need to respect our application lifecycle, we will only get the user's location once we have active observers. Otherwise, we do nothing.
     */
    private MutableLiveData<Location> locationStream;

    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    /*
     * Location update speed.
     * The update interval - refers to the regular interval the device will ask for updates
     * the fastest interval - refers to the max speed at which updates will be queried. we will not go beyond this threshold set
     */
    private static final long LOCATION_UPDATE_INTERVAL_MILLIS = 300000; //request location every 5 minutes
    private static final long FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS = LOCATION_UPDATE_INTERVAL_MILLIS / 2;


    @Inject
    public LocationUtils(Context context, LocationRequest locationRequest) {

        this.context = context;
        this.locationRequest = locationRequest;
        this.locationStream = new MutableLiveData<>();
    }

    @SuppressWarnings("MissingPermission")
    private void createLocationRequest() {

        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MILLIS);
        locationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void createLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                locationStream.setValue(currentLocation);
            }
        };
    }



    @SuppressWarnings("MissingPermission")
    public LiveData<Location> getUserLocation() {

            createLocationCallback();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            createLocationRequest();

            return locationStream;
    }

    /*
     * We'll post any information we have for the locationProvider to the log for debugging purposes.
     */
    private void locationInfoLogger(String logMessage){

        Log.w(LOG_TAG,  logMessage);
    }

}
