package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
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
       ButterKnife.bind(getActivity());

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

            getData();

        } else {

            Toast.makeText(getContext(), "Unable to connect to service. Please check your connection and try again", Toast.LENGTH_LONG).show();
        }

    }

    /*
     * Internet connection has been confirmed to be working, lets try and grab the user data
     * If this is unsuccessful, something else beyond our control is most likely down
     */
    private void getData() {

        viewModel.getWeather().observe(this, weatherInfo -> {

            if(weatherInfo != null) {

                Log.i(LOG_TAG, "got info!");

                updateUI(weatherInfo);

            } else {

                Toast.makeText(getContext(), "Unable to get Weather Information. Try again later", Toast.LENGTH_LONG).show();

                Log.w(LOG_TAG, "Unable to get weather!");
            }

        });

    }

    private void updateUI(ForecastResponse weatherInfo) {

        /*
         * Weather Icon to set for the app
         */

        int weatherId = weatherInfo.getForecastList().get(0).getWeather().get(0).getIconId();
        int weatherImageId = CloudyWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);



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
        binding.tvHumidity.setText(humidityString);
        binding.tvPressure.setText(pressureString);
        binding.tvWindInfo.setText(windSpeedString);
        binding.imgForecastCondition.setImageResource(weatherImageId);

    }


}
