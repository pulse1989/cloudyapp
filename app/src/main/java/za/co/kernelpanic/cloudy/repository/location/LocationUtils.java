package za.co.kernelpanic.cloudy.repository.location;


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

    /*
     * Our application context -  courtesy of Dagger
     */
    private Context context;

    /*
     *  We declare the location Mutable LiveData here as this is responsible for updating our weather request to any observers that are listening.
     *  Because we need to respect our application lifecycle, we will only get the user's location once we have active observers. Otherwise, we do nothing.
     */
    private MutableLiveData<Location> locationStream;

    /*
     * Location variable for getting the user's location with properties such as accuracy etc
     */
    private Location currentLocation;
    /*
     * Location request - used to set parameters to our location request such as update intervals (specified below)
     */
    private LocationRequest locationRequest;

    /*
     * Where our location data is going to be sent. We need that callback to set values to our LiveData
     */
    private LocationCallback locationCallback;

    /*
     * Location update speed.
     * The update interval - refers to the regular interval the device will ask for updates. Default is 5 minutes while the app is open
     * the fastest interval - refers to the max speed at which updates will be queried - 2.5 minutes. we will not go beyond this threshold set
     */
    private static final long LOCATION_UPDATE_INTERVAL_MILLIS = 300000;
    private static final long FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS = LOCATION_UPDATE_INTERVAL_MILLIS / 2;

    /*
     * Setup for the class:
     * We need our LiveData to stream our location updates to our repository
     * We also need our location request param (Supplied by dagger) to setup additional parameters on the location
     * Lastly, we need our application context in order to use our FusedLocationClient within this class.
     */
    @Inject
    public LocationUtils(Context context, LocationRequest locationRequest) {

        this.context = context.getApplicationContext();
        this.locationRequest = locationRequest;
        this.locationStream = new MutableLiveData<>();

    }

    /*
     * We use our location request to setup additional parameters for our location request, we also start getting location updates here.
     * The default location update request is going to be 5 minutes. Once the user's viewmodel unsubscribes to our LiveData, we stop requesting updates.
     */
    @SuppressWarnings("MissingPermission")
    private void createLocationRequest() {

        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MILLIS);
        locationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        locationInfoLogger("Initiated location request");

    }

    /*
     * Our location callback that receives the request updates from the locationRequest method above.
     * The location grabbed is then given to our livedata via the .setValue() method which it streams back to the repository
     */

    private void createLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                locationStream.setValue(currentLocation);
                locationInfoLogger("We've gotten a location, sending to repository");
            }
        };
    }


    /*
     * Our LiveData that's responsible for sending our user location updates to our repository class.
     * one the repository has the user's location, they're able to use that for any additional requests such as
     * network calls
     */
    @SuppressWarnings("MissingPermission")
    public LiveData<Location> getUserLocation() {

        createLocationCallback();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationInfoLogger("Fused location client initialized successfully");
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
