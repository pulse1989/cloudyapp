package za.co.kernelpanic.cloudy.repository.remote;

import androidx.lifecycle.LiveData;
import za.co.kernelpanic.cloudy.data.ForecastResponse;


/*
 * We need to implement this interface in order to use the fetchWeather using retrofit
 */
public interface WeatherNetworkInterface {

     LiveData<ForecastResponse> fetchWeatherForecast(double latitude, double longitude);
}
