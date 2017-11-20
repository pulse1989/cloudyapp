package za.co.kernelpanic.cloudy.utils.di.dagger.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import za.co.kernelpanic.cloudy.ui.activities.WeatherActivity;
import za.co.kernelpanic.cloudy.utils.di.dagger.submodules.FragmentBuildersModule;

@Module
public abstract class WeatherActivityModule {

    @ContributesAndroidInjector (modules = {FragmentBuildersModule.class})
    abstract WeatherActivity contributesWeatherActivity();
}
