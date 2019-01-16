package za.co.kernelpanic.cloudy.repository.main;

import androidx.lifecycle.LiveData;
import android.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.location.LocationUtils;
import za.co.kernelpanic.cloudy.repository.remote.WeatherNetworkDataSource;

/*
 *  Single source of truth for our application. We handle getting the user location, getting the information from the API.
 *  We can also insert values in tht the database from here once we have the forecast data. (skipping this step for now)
 */
@Singleton
public class CloudyRepository implements CloudyRepoInterface {

    private static final String LOG_TAG = CloudyRepository.class.getSimpleName();

    /*
     * Location requests are handled by LocationUtilsDep class, we get it from there
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

    @Override
    public void shutdownLocationUpdates() {

        locationUtils.stopLocationUpdates();
    }


}