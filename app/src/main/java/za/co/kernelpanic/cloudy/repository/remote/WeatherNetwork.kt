package za.co.kernelpanic.cloudy.repository.remote

import androidx.lifecycle.LiveData
import za.co.kernelpanic.cloudy.data.ForecastResponse

interface WeatherNetwork {

    /*
    * We need to implement this interface in order to use the fetchWeather using retrofit
    */

    fun fetchWeatherForecast(latitude: Double, longitude: Double): LiveData<ForecastResponse>
}