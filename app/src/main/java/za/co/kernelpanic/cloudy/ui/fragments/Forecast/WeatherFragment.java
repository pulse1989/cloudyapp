package za.co.kernelpanic.cloudy.ui.fragments.Forecast;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import za.co.kernelpanic.cloudy.R;
import za.co.kernelpanic.cloudy.databinding.FragmentWeatherForecastBinding;
import za.co.kernelpanic.cloudy.utils.WeatherViewModelFactory;


public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();
    private FragmentWeatherForecastBinding binding;


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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherForecastViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_forecast, container, false);
       viewModel.getLastLocation().observe(this, weatherInfo -> {

           if(weatherInfo != null) {

               viewModel.tryGettingWeatherInfo(weatherInfo.getLongitude(), weatherInfo.getLatitude());

               Log.w(LOG_TAG, "Longitude: " + weatherInfo.getLongitude() + " Latitude: " + weatherInfo.getLatitude());
               viewModel.getWeatherInfoReal();

           } else {

               Log.w(LOG_TAG, "failed to get location");
           }

       });
       return binding.getRoot();
    }

}
