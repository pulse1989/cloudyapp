package za.co.kernelpanic.cloudy.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

import javax.inject.Inject

/**
 * This class is responsible for getting the device location as requested by our repository.
 * The ViewModel would then use this data to send the remote request. The UI  will be loading during this process
 */

class LocationUtils @Inject constructor(private val context: Context, private val locationRequest: LocationRequest) {

    companion object {
        private const val LOCATION_UPDATE_INTERVAL_IN_MILLIS: Long = 30000
        private const val FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS = LOCATION_UPDATE_INTERVAL_IN_MILLIS / 2
    }

    private val LOG_TAG: String = LocationUtils::class.java.simpleName

    /*
     *  We declare the location Mutable LiveData here as this is responsible for updating our weather request to any observers that are listening.
     *  Because we need to respect our application lifecycle, we will only get the user's location once we have active observers. Otherwise, we do nothing.
     */
    private val locationStream = MutableLiveData<Location>()

    /*
    * Location variable for getting the user's location with properties such as accuracy etc
    */
    private var currentLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    /*
     * We have access to the user's location via this API. It requires google play services to be installed on the device.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /*
     * Our location callback that receives the request updates from the locationRequest method above.
     * The location grabbed is then given to our livedata via the .setValue() method which it streams back to the repository
     */
    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult?.lastLocation
                locationStream.value = currentLocation
                locationInfoLogger("We've gotten a location, sending to repository")
            }
        }
    }

    /*
     * We use our location request to setup additional parameters for our location request, we also start getting location updates here.
     * The default location update request is going to be 5 minutes. Once the user's viewmodel unsubscribes to our LiveData, we stop requesting updates.
     */
    @SuppressLint("MissingPermission")
    private fun createLocationRequest() {
        locationRequest.interval = LOCATION_UPDATE_INTERVAL_IN_MILLIS
        locationRequest.fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL_IN_MILLIS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    /*
     * Our LiveData that's responsible for sending our user location updates to our repository class.
     * one the repository has the user's location, they're able to use that for any additional requests such as
     * network calls
     */
    fun getUserLocation(): LiveData<Location> {
        createLocationCallback()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationInfoLogger("Fused location client initialized successfully")
        createLocationRequest()
        return locationStream
    }

    /*
     * We'll post any information we have for the locationProvider to the log for debugging purposes.
     */
    private fun locationInfoLogger(logMessage: String) {
        Log.w(LOG_TAG, logMessage)
    }

    /*
     * Once we've given the user the relevant information with regards to the weather, we can stop listening to location
     * updates by calling this method.
     */

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        locationInfoLogger("Fused location client stopped successfully")
    }
}