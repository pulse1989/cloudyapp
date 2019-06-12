package za.co.kernelpanic.cloudy.ui.fragments.forecast

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import za.co.kernelpanic.cloudy.data.ForecastResponse
import za.co.kernelpanic.cloudy.repository.main.CloudyRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Our ViewModel for calling the weather api service for getting our 7 day forecast for the user
 *
 * We're only getting the current day for now (time is of the essence..)
 */
@Singleton
class WeatherForecastViewModel @Inject //provided by dagger via the map
constructor(private val cloudyRepository: CloudyRepositoryImpl) : ViewModel() {

    /*
     * This Livedata is responsible for getting a user's location using the locationUtil's class.
     * It's called by our weatherMethod because essentially, this is a dependency...
     */
    private val lastLocation: LiveData<Location>
        get() = cloudyRepository.getUserLocation()

    /*
     * We have two Livedata objects working in this class:
     * 1. Before we get the weather we need the user's location (done in the above method).
     *
     * 2. Once we get the user's location, we can send that through to our forecast method with the correct params (latitude and longitude.
     *   This will then go to the weather service and get the data that we can send back to our observers (The WeatherFragment)
     *
     *  Transformations (gold!) allow us to listen to one LiveData, then once we get the required information from there, we can then use that for the next LiveData (Weather Forecast).
     *  All this is done while respecting the Lifecycle of the application :D  (as Livedata objects are lifecycle aware)
     */
    val weather: LiveData<ForecastResponse>
        get() =
            Transformations.switchMap(lastLocation) { location -> cloudyRepository.getForecast(location.latitude, location.longitude) }

    fun removeLocationUpdates() {
        cloudyRepository.shutdownLocationUpdates()
    }
}
