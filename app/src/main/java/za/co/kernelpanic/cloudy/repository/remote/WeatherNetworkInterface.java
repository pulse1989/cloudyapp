package za.co.kernelpanic.cloudy.repository.remote;

import android.arch.lifecycle.LiveData;

import za.co.kernelpanic.cloudy.data.ForecastResponse;



public interface WeatherNetworkInterface {

     String onError(Throwable throwable);
     LiveData<ForecastResponse> provideWeatherForecast();
     void handleResponse(ForecastResponse forecastResponse);
     void sendRequest(double longitude, double latitude);

}
