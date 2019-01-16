package za.co.kernelpanic.cloudy.repository.main;

import androidx.lifecycle.LiveData;
import android.location.Location;

import za.co.kernelpanic.cloudy.data.ForecastResponse;

public interface CloudyRepoInterface {

    /*
     * Repository interface for our Concrete implementation of this class
     */

    LiveData<Location> getUserLocation();
    LiveData<ForecastResponse> getForecast(double latitude, double longitude);
    void shutdownLocationUpdates();
}
