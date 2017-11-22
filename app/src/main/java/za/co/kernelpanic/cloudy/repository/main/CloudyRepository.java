package za.co.kernelpanic.cloudy.repository.main;

import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import za.co.kernelpanic.cloudy.repository.location.LocationUtils;
import za.co.kernelpanic.cloudy.repository.remote.WeatherNetworkDataSource;

@Singleton
public class CloudyRepository implements CloudyRepoInterface {

    private static final String LOG_TAG = CloudyRepository.class.getSimpleName();

    /*
     * Location requests are handled by LocationUtils class, we get it from there
     */

    private final LocationUtils locationUtils;

    /*
     * Api specific variables. these are required in the path as parameters during our api call
     */

    private WeatherNetworkDataSource weatherNetworkDataSource;


    /*
     * We use constructor injection here to initialize all our variables before we continue (as with the rest of the app;
     */
    @Inject
    public CloudyRepository(LocationUtils locationUtils, WeatherNetworkDataSource weatherNetworkDataSource){

        this.locationUtils = locationUtils;
        this.weatherNetworkDataSource = weatherNetworkDataSource;

    }

    @Override
    public void initializeData(double longitude, double latitude) {

        weatherNetworkDataSource.sendRequest(longitude, latitude);

    }

    @Override
    public LiveData<Location> getUserLocation() {

        return locationUtils.getUserLocation();
    }

    //TODO - fix the final piece of the puzzle
    @Override
    public void getForecast() {

        Log.w(LOG_TAG, "Weather request called " + weatherNetworkDataSource.provideWeatherForecast().getValue().getCity().getName());
        weatherNetworkDataSource.provideWeatherForecast().getValue();
    }


}