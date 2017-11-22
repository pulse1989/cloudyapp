package za.co.kernelpanic.cloudy.repository.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import za.co.kernelpanic.cloudy.data.ForecastResponse;


public class WeatherNetworkDataSource implements  WeatherNetworkInterface {

    private static final String LOG_TAG = WeatherNetworkDataSource.class.getSimpleName();

    private final WeatherApi weatherApi;
    private final CompositeDisposable compositeDisposable;

    private static final String API_KEY = "625546691b9f757f1da64b49d9740289";
    private static final String REQUEST_MODE = "json";
    private static final String REQUEST_UNITS = "metric";
    private static final int REQUEST_FORECAST_DAYS = 1;
    private MutableLiveData<ForecastResponse> responseLiveData;

    /*
     * This is called from the repository class.
     * We use the sendRequest
     */

    @Inject
    public WeatherNetworkDataSource(WeatherApi weatherApi, CompositeDisposable compositeDisposable){

        this.weatherApi = weatherApi;
        this.compositeDisposable = compositeDisposable;
        this.responseLiveData = new MutableLiveData<>();

    }


    @Override
    public void sendRequest(double longitude, double latitude) {

        compositeDisposable.add(weatherApi.getForecast(API_KEY, latitude, longitude, REQUEST_MODE, REQUEST_UNITS, REQUEST_FORECAST_DAYS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(this::handleResponse, this::onError));
    }


    @Override
    public String onError(Throwable throwable) {

        String error = "Unable to get weather Info";

        Log.e(LOG_TAG, "Unable to get data: " + throwable.getMessage());

        return error;
    }

    @Override
    public void handleResponse(ForecastResponse forecastResponse) {

       Log.w(LOG_TAG, "setting the livedata with stuff: " + forecastResponse.getCity().getName());
       Log.w(LOG_TAG, "getting daily temp: " + forecastResponse.getForecastList().get(0).getPressure());
       responseLiveData.setValue(forecastResponse);
    }


    @Override
    public LiveData<ForecastResponse> provideWeatherForecast() {

        Log.w(LOG_TAG, "live data method: " + responseLiveData);
        return responseLiveData;

    }



}
