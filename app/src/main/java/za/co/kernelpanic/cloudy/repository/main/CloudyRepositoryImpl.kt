package za.co.kernelpanic.cloudy.repository.main

import android.location.Location
import androidx.lifecycle.LiveData
import za.co.kernelpanic.cloudy.data.ForecastResponse
import za.co.kernelpanic.cloudy.repository.location.LocationUtils
import za.co.kernelpanic.cloudy.repository.remote.WeatherNetworkDataSourceImpl
import javax.inject.Inject
import javax.inject.Singleton

/*
 *  Single source of truth for our application. We handle getting the user location, getting the information from the API.
 *  We can also insert values in tht the database from here once we have the forecast data. (skipping this step for now)
 */

/*
 * We use constructor injection here to initialize all our variables before we continue (as with the rest of the app;
 */

@Singleton
class CloudyRepositoryImpl @Inject constructor(private val locationUtils: LocationUtils, private val weatherNetworkDataSourceImpl: WeatherNetworkDataSourceImpl) : CloudyRepository {

    override fun getUserLocation(): LiveData<Location> {
        return locationUtils.getUserLocation()
    }

    override fun getForecast(latitude: Double, longitude: Double): LiveData<ForecastResponse> {
        return weatherNetworkDataSourceImpl.fetchWeatherForecast(latitude, longitude)
    }

    override fun shutdownLocationUpdates() {
        locationUtils.stopLocationUpdates()
    }
}