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


import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;


/**
 * This class is responsible for getting the device location as requested by the viewmodel
 * The ViewModel would then use this data to send the remote request. The UI  will be loading during this process
 */

public class LocationUtils {

    private static final String  LOG_TAG = LocationUtils.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;
    private MutableLiveData<Location> lastLocation;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private static final long LOCATION_UPDATE_INTERVAL_MILLIS = 1000; //request location every 5 minutes
    private static final long FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS = LOCATION_UPDATE_INTERVAL_MILLIS / 2;

    private String lastUpdateTime;


    @Inject
    public LocationUtils(Context context) {

        this.context = context;
        lastLocation = new MutableLiveData<>();

        if(fusedLocationClient == null) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

            /*
             * Until we start getting decent locations, let's get the current location for now.
             * It could be crap but what choice do we have? :)
             */

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    currentLocation = locationResult.getLastLocation();
                    lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    postLastUpdateTime(lastUpdateTime);
                    Log.w(LOG_TAG, "constructor: latitude: " + currentLocation.getLatitude() + " Longitude: " + currentLocation.getLongitude());

                }
            };
        }
    }

    /**
     *
     * Because location updates are governed by LiveData, once the activity is in a stopped state, updates are also stopped.
     * Once The activity is resumed, the location updates are done every 5 minutes. We really don't need updates that much as we're
     * checking the weather, however get last known location can also be inaccurate
     */
    @SuppressWarnings("MissingPermission")
    public LiveData<Location> getUserLocation() {

        //todo - fix get user location, it never sends any updates
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MILLIS);
        locationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()).addOnCompleteListener(task -> {

              if(task.isSuccessful() && task.getResult() != null){

                  lastLocation.postValue(currentLocation);
                  lastUpdateTime  = "some shit from the main method";
                  lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                  postLastUpdateTime(lastUpdateTime);

                  Log.w(LOG_TAG, "getUserLocation() latitude: " + currentLocation.getLatitude() + " Longitude: " + lastLocation.getValue().getLongitude());
              }

          }).addOnFailureListener( failure -> {

              Log.e(LOG_TAG, "We failed to get updates -- no-op");

          });

            return lastLocation;
    }

    private void postLastUpdateTime(String lastUpdateTime){

        Log.i(LOG_TAG, "Location last updated: " + lastUpdateTime);
    }

}
