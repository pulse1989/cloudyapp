package za.co.kernelpanic.cloudy.repository.main;

import android.arch.lifecycle.LiveData;
import android.location.Location;

import za.co.kernelpanic.cloudy.data.ForecastResponse;

public interface CloudyRepoInterface {


    LiveData<Location> getUserLocation();
    LiveData<ForecastResponse> getForecast(double latitude, double longitude);
}
