package za.co.kernelpanic.cloudy.repository.remote;

import android.arch.lifecycle.LiveData;

import za.co.kernelpanic.cloudy.data.ForecastResponse;



public interface WeatherNetworkInterface {

     LiveData<ForecastResponse> fetchWeatherForecast(double latitude, double longitude);
}
