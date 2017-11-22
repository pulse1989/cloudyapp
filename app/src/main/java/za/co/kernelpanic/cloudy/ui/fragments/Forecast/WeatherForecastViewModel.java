package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.main.CloudyRepository;


/**
 * Our ViewModel for calling the weather api service for getting our 7 day forecast for the user
 */
@Singleton
public class WeatherForecastViewModel extends ViewModel {

    private static final String LOG_TAG = WeatherForecastViewModel.class.getSimpleName();
    private CloudyRepository cloudyRepository;
    private LiveData<ForecastResponse> forecastLiveData;


    @Inject //provided by dagger via the map
    public WeatherForecastViewModel(CloudyRepository repository) {
        this.cloudyRepository = repository;
    }

    public LiveData<Location> getLastLocation(){

        return cloudyRepository.getUserLocation();
    }

    public void tryGettingWeatherInfo (double longitude, double latitude){

        cloudyRepository.initializeData(longitude, latitude);
    }

    public void getWeatherInfoReal(){

        cloudyRepository.getForecast();
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
