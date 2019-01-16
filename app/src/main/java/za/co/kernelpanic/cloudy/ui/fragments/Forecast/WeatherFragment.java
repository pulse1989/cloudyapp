package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import za.co.kernelpanic.cloudy.R;
import za.co.kernelpanic.cloudy.data.ForecastResponse;
import za.co.kernelpanic.cloudy.databinding.FragmentWeatherForecastBinding;
import za.co.kernelpanic.cloudy.utils.CloudyWeatherUtils;
import za.co.kernelpanic.cloudy.utils.WeatherViewModelFactory;


public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();

    //data binding
    private FragmentWeatherForecastBinding binding;


    //we need to check for network connectivity before trying to get data
    private boolean isConnected;

    /*
     * Our loading indicator
     */

    private ProgressBar pendingProgress;

    /*
     * We then inject our two special lads here in order to make this work,
     */
    @Inject WeatherViewModelFactory viewModelFactory;
    @Inject WeatherForecastViewModel viewModel;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /*
         * In order for our business logic layer to respect this UI lifecycle, we bind our viewmodel to the UI.
         * This way even during configuration and other state changes. the app will be fine.
         */
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherForecastViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment using databinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_forecast, container, false);
        pendingProgress = binding.appProgressIndicator;
        setHasOptionsMenu(true);
        checkConnectivity();
        return binding.getRoot();
    }


    /*
     * Let's make sure the user has a working internet connection before we attempt to get the weather data.
     * We save battery life by not invoking any location requests here. We also notify the user that they should check
     * their settings.
     */
    private void checkConnectivity() {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null) {

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        } else {

            Log.e(LOG_TAG, "Unable to get network info");
        }

        Log.w(LOG_TAG, "Device Network state: Connection -> " + isConnected);

        if(isConnected) {

            showLoading();

        } else {

            showSnackBar("Unable to connect to service. Please check your connection and try again");

            /*
             * Because it's a sad moment...
             */
            binding.imgCurrentWeather.setImageResource(R.drawable.weather_loading_fail);
        }
    }

    /*
     * Show a loading screen while we retrieve data.
     */
    private void showLoading() {

        pendingProgress.setVisibility(View.VISIBLE);
        binding.imgCurrentWeather.setVisibility(View.INVISIBLE);
        getData();
    }

    /*
     * Internet connection has been confirmed to be working, lets try and grab the user data
     * If this is unsuccessful, something else beyond our control is most likely down
     */
    private void getData() {

        viewModel.getWeather().observe(this, weatherInfo -> {

            if(weatherInfo != null && weatherInfo.getForecastList().size() > 0) {

                Log.i(LOG_TAG, "got info!");
                hideLoading();
                updateUI(weatherInfo);

            } else {

               showSnackBar("Unable to get Weather Information. Try again later");

                Log.w(LOG_TAG, "Unable to get weather!");
            }

        });

    }

    private void hideLoading() {

        pendingProgress.setVisibility(View.INVISIBLE);
        binding.imgCurrentWeather.setVisibility(View.VISIBLE);
    }

    private void updateUI(ForecastResponse weatherInfo) {

        /*
         * Weather Icon to set for the app
         */

        int weatherId = weatherInfo.getForecastList().get(0).getWeather().get(0).getIconId();
        int weatherImageId = CloudyWeatherUtils.getLargeCurrentWeatherArt(weatherId);


        /*
         * Location specific info
         */
        String weatherLocation = weatherInfo.getCity().getName();
        String weatherDescription = weatherInfo.getForecastList().get(0).getWeather().get(0).getDescription();

        /*
         * Our core temperature info:
         */
        double temperature = weatherInfo.getForecastList().get(0).getTemperature().getDayForecast();
        double highTemp = weatherInfo.getForecastList().get(0).getTemperature().getMaxTemp();
        double lowTemp = weatherInfo.getForecastList().get(0).getTemperature().getMinTemp();

        String currentTemp = CloudyWeatherUtils.formatTemperature(getActivity(), temperature);
        String maxTemp  = CloudyWeatherUtils.formatTemperature(getActivity(), highTemp);
        String minTemp = CloudyWeatherUtils.formatTemperature(getActivity(), lowTemp);


        /*
         * Pressure and humidity
         */
        double pressure = weatherInfo.getForecastList().get(0).getPressure();
        double humidity = weatherInfo.getForecastList().get(0).getHumidity();
        String humidityString = getString(R.string.format_humidity, humidity);
        String pressureString = getString(R.string.format_pressure, pressure);


        /*
         * Wind information
         */

        double windSpeed = weatherInfo.getForecastList().get(0).getWindSpeed();
        double windDirection = weatherInfo.getForecastList().get(0).getWindDirection();

        String windSpeedString = CloudyWeatherUtils.getFormattedWind(getActivity(), windSpeed, windDirection);

        //we now have to bind all our awesome variables to each of their views so we can see the weather
        binding.tvLocationHeader.setText(weatherLocation);
        binding.tvWeatherDescription.setText(weatherDescription);
        binding.tvWeatherTemp.setText(currentTemp);
        binding.tvMinTempDetail.setText(minTemp);
        binding.maxTempDetail.setText(maxTemp);
        binding.tvHumidityDetail.setText(humidityString);
        binding.tvAirPressureDetail.setText(pressureString);
        binding.tvWindSpeedDetail.setText(windSpeedString);
        binding.imgCurrentWeather.setImageResource(weatherImageId);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main, menu);
    }

    /*
     * Whenever the refresh icon is tapped, this method is triggered.
     * As always, we check for network connectivity.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_refresh:
                showSnackBar("Refreshing...");
                checkConnectivity();

                Log.i(LOG_TAG, "Refreshing weather info");

            default:
                return super.onOptionsItemSelected(item); //superclass will handle default options
        }
    }

    /*
     * Default message method for showing information to the user.
     */
    private void showSnackBar(String message){

        if(getView() != null){

            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();

        } else {


            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPause() {

        viewModel.removeLocationUpdates();
        super.onPause();
    }
}
