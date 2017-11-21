package za.co.kernelpanic.cloudy.repository;


import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.util.Log;
import android.util.TimeUtils;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.schedulers.Schedulers;
import za.co.kernelpanic.cloudy.data.Forecast;
import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.db.ForecastDao;
import za.co.kernelpanic.cloudy.repository.remote.WeatherApi;
import za.co.kernelpanic.cloudy.utils.LocationUtils;

/**
 * This class is responsible for the database, network and user location requests.
 * First we check if the database has the forecast for the next 7 days by checking the database. If not,
 * we acquire the user's location before sending the request. The flow is handled by the viewmodel.
 */

@Singleton
public class CloudyRepository {

    private static final String LOG_TAG = CloudyRepository.class.getSimpleName();
    private final WeatherApi weatherApi;
    private final LocationUtils locationUtils;
    private final CompositeDisposable disposable; // living life on the edge...
    private static final String API_KEY = "625546691b9f757f1da64b49d9740289";
    private static final String MODE = "json";
    private static final String UNITS = "metric";
    private static final int FORECAST_DAYS = 7;

    @Inject
    public CloudyRepository(WeatherApi weatherApi, LocationUtils locationUtils, CompositeDisposable disposable){

        this.weatherApi = weatherApi;
        this.locationUtils = locationUtils;
        this.disposable = disposable;


    }

    //TODO - fix this shit - we should try and stay off the main thread until we're done writing to the database.

    public void initializeDate(double longitude, double latitude){

        Log.w(LOG_TAG, "initializing date, longitude: " + String.valueOf(longitude) + " Latitude: " + String.valueOf(latitude));
        disposable.add(getWeatherForecastFromSource(longitude, latitude)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe(this::readWeatherForecastFromToday, this::onError));
    }

    private void readWeatherForecastFromToday(ForecastResponse forecastResponse) {

        Log.w(LOG_TAG, "API RESPONSE: " + forecastResponse);
    }

    //we can use a LiveData to get the users location
    public LiveData<Location> userLocation() {

        return locationUtils.getUserLocation();
    }

    //We can also use this to get the weather forecast
    public Flowable<ForecastResponse> getWeatherForecastFromSource(double latitude, double longitude) {

        return  weatherApi.getForecast(API_KEY, latitude, longitude, MODE, UNITS, FORECAST_DAYS);
    }

//    public LiveData<Forecast> readWeatherForecastFromToday(Date date) {
//
//    }

    //TODO - get rid
    private void onError(Throwable throwable){

        Log.e(LOG_TAG, "we failed to get the weather.\n " + throwable.getMessage());
    }

    //we can query the database with this method:

}
