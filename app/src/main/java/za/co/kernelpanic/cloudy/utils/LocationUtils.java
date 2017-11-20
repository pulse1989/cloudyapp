package za.co.kernelpanic.cloudy.utils;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;


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

    @Inject
    public LocationUtils(Context context) {

        this.context = context;
        lastLocation = new MutableLiveData<>();

        if(fusedLocationClient == null) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            locationCallback = new LocationCallback();
        }
    }

    @SuppressWarnings("MissingPermission")
    public LiveData<Location> getUserLocation() {

        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null) {

                lastLocation.setValue(task.getResult());
            }
        });

        return lastLocation;
    }
}
