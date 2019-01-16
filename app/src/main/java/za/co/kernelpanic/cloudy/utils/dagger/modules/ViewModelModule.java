package za.co.kernelpanic.cloudy.utils.dagger.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherForecastViewModel;
import za.co.kernelpanic.cloudy.utils.WeatherViewModelFactory;

@Module
public abstract class ViewModelModule {

    /*
     * This ViewModel will be used by the factory in order to be created for the class that needs it (our WeatherActivityFragment)
     * @Binds == @Provides. it's just more efficient and is used for abstract classes.
     * @IntoMap  - add's it to a map to be used by dagger(This will be the value, an instance of the viewmodel;
     * @ViewModelKey - Our own annotation that tells dagger which class to use as the key for our map. this class HAS to extend ViewModel or instantiation will fail
     * Within your UI class just call @Inject on the viewmodel to begin using it.
     */
    @Binds @IntoMap @ViewModelKey(WeatherForecastViewModel.class)
    abstract ViewModel providesWeatherForecastViewModel(WeatherForecastViewModel weatherForecastViewModel);

    /*
     * Our ViewModel factory. Any viewmodels passed into the factory from this class will be provided to the class that needs it by dagger.
     * Call @Inject on the creation of this factory into your UI in order to use it with the factory
     */

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelFactory viewModelFactory);
}
