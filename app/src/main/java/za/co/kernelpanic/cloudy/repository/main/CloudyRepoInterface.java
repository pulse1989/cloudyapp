package za.co.kernelpanic.cloudy.repository.main;

import androidx.lifecycle.LiveData;
import za.co.kernelpanic.cloudy.data.ForecastResponse;

import android.location.Location;

public interface CloudyRepoInterface {

    /*
     * Repository interface for our Concrete implementation of this class
     */

    LiveData<Location> getUserLocation();
    LiveData<ForecastResponse> getForecast(double latitude, double longitude);
    void shutdownLocationUpdates();
}
