package za.co.kernelpanic.cloudy.repository;


import android.arch.lifecycle.LiveData;
import android.location.Location;

import javax.inject.Inject;

import io.reactivex.Single;
import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.remote.WeatherApi;
import za.co.kernelpanic.cloudy.utils.LocationUtils;

/**
 * This class is responsible for the database, network and user location requests.
 * First we check if the database has the forecast for the next 7 days by checking the database. If not,
 * we acquire the user's location before sending the request. The flow is handled by the viewmodel.
 */

public class CloudyRepository {

    private static final String LOG_TAG = CloudyRepository.class.getSimpleName();
    private final WeatherApi weatherApi;
    private final LocationUtils locationUtils;
    private static final String API_KEY = "625546691b9f757f1da64b49d9740289";
    private static final String MODE = "json";
    private static final String UNITS = "metric";
    private static final int FORECAST_DAYS = 7;

    @Inject
    public CloudyRepository(WeatherApi weatherApi, LocationUtils locationUtils){

        this.weatherApi = weatherApi;
        this.locationUtils = locationUtils;


    }

    //we can use a LiveData to get the users location
    public LiveData<Location> userLocation() {

        return locationUtils.getUserLocation();
    }

    //We can also use this to get the weather forecast
    public Single<ForecastResponse> getWeatherForecast(double latitude, double longitude) {

        return  weatherApi.getForecast(API_KEY, latitude, longitude, MODE, UNITS, FORECAST_DAYS);
    }

}
