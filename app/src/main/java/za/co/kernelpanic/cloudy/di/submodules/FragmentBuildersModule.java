package za.co.kernelpanic.cloudy.di.submodules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherFragment;

@Module
public  abstract class FragmentBuildersModule {

    /*
     * Our activity will be able to inject this using dagger
     */
    @ContributesAndroidInjector
    abstract WeatherFragment weatherFragment();
}
