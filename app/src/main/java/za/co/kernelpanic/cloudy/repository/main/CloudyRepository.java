package za.co.kernelpanic.cloudy.repository.main;

import android.arch.lifecycle.LiveData;
import android.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

import za.co.kernelpanic.cloudy.data.ForecastResponse;
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
    public LiveData<Location> getUserLocation() {

        return locationUtils.getUserLocation();
    }

    @Override
    public LiveData<ForecastResponse> getForecast(double latitude, double longitude) {

         return  weatherNetworkDataSource.fetchWeatherForecast(latitude, longitude);

    }





}