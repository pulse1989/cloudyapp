package za.co.kernelpanic.cloudy.repository.main;

import android.arch.lifecycle.LiveData;
import android.location.Location;

public interface CloudyRepoInterface {


    void initializeData(double Longitude, double Latitude);
    LiveData<Location> getUserLocation();
    void getForecast();
}
