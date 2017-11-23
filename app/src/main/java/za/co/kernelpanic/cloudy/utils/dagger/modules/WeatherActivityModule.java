package za.co.kernelpanic.cloudy.utils.dagger.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import za.co.kernelpanic.cloudy.ui.activities.WeatherActivity;
import za.co.kernelpanic.cloudy.utils.dagger.submodules.FragmentBuildersModule;

@Module
public abstract class WeatherActivityModule {

    /*
     * by
     */
    @ContributesAndroidInjector (modules = {FragmentBuildersModule.class})
    abstract WeatherActivity contributesWeatherActivity();
}
