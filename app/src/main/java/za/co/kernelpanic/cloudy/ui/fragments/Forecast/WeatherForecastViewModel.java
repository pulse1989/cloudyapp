package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import android.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.repository.main.CloudyRepository;


/**
 * Our ViewModel for calling the weather api service for getting our 7 day forecast for the user
 *
 * We're only getting the current day for now (time is of the essence..)
 */
@Singleton
public class WeatherForecastViewModel extends ViewModel {

    private static final String LOG_TAG = WeatherForecastViewModel.class.getSimpleName();
    private CloudyRepository cloudyRepository;
    private LiveData<ForecastResponse> forecastData;

    @Inject //provided by dagger via the map
    public WeatherForecastViewModel(CloudyRepository repository) {
        this.cloudyRepository = repository;
    }

    /*
     * This Livedata is responsible for getting a user's location using the locationUtil's class.
     * It's called by our weatherMethod because essentially, this is a dependency...
     */
    private LiveData<Location> getLastLocation(){

        return cloudyRepository.getUserLocation();
    }

    /*
     * We have two Livedata objects working in this class:
     * 1. Before we get the weather we need the user's location (done in the above method).
     *
     * 2. Once we get the user's location, we can send that through to our forecast method with the correct params (latitude and longitude.
     *   This will then go to the weather service and get the data that we can send back to our observers (The WeatherFragment)
     *
     *  Transformations (gold!) allow us to listen to one LiveData, then once we get the required information from there, we can then use that for the next LiveData (Weather Forecast).
     *  All this is done while respecting the Lifecycle of the application :D  (as Livedata objects are lifecycle aware)
     */
    public LiveData<ForecastResponse> getWeather() {

        forecastData = Transformations.switchMap(getLastLocation(), location -> cloudyRepository.getForecast(location.getLatitude(), location.getLongitude()));

        return forecastData;
    }

    public void removeLocationUpdates() {

        cloudyRepository.shutdownLocationUpdates();
    }



}
