package za.co.kernelpanic.cloudy.utils.di.dagger.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherForecastViewModel;
import za.co.kernelpanic.cloudy.utils.WeatherViewModelFactory;

@Module
public abstract class ViewModelModule {

    @Binds @IntoMap @ViewModelKey(WeatherForecastViewModel.class)
    abstract ViewModel providesWeatherForecastViewModel(WeatherForecastViewModel weatherForecastViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelFactory viewModelFactory);
}
