package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.util.Log;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.CompositeDisposable;
import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.CloudyRepository;
import za.co.kernelpanic.cloudy.utils.LocationUtils;


/**
 * Our ViewModel for calling the weather api service for getting our 7 day forecast for the user
 */
@Singleton
public class WeatherForecastViewModel extends ViewModel {

    private static final String LOG_TAG = WeatherForecastViewModel.class.getSimpleName();
    private CloudyRepository cloudyRepository;
    private LiveData<ForecastResponse> forecastLiveData;
    private CompositeDisposable compositeDisposable;


    @Inject //provided by dagger via the map
    public WeatherForecastViewModel(CloudyRepository repository, CompositeDisposable compositeDisposable) {
        this.cloudyRepository = repository;
        this.compositeDisposable = compositeDisposable;

    }

    public LiveData<Location> getLastLocation(){

        return cloudyRepository.userLocation();
    }



//    public LiveData<ForecastResponse>  getWeather() {
//
//         forecastLiveData = Transformations.switchMap(cloudyRepository.userLocation(), location -> {
//
//             double latitude = location.getLatitude();
//             double longitude = location.getLongitude();
//
//             return cloudyRepository.getWeatherForecast(latitude, longitude);
//         });
//
//         return forecastLiveData;
//    }

}
