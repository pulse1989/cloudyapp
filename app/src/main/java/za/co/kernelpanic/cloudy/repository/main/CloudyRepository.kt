package za.co.kernelpanic.cloudy.repository.main

import android.location.Location
import androidx.lifecycle.LiveData
import za.co.kernelpanic.cloudy.data.ForecastResponse

interface CloudyRepository {

    /*
     * Repository interface for our Concrete implementation of this class
     */
    fun getUserLocation(): LiveData<Location>
    fun getForecast(latitude: Double, longitude: Double): LiveData<ForecastResponse>
    fun shutdownLocationUpdates()
}