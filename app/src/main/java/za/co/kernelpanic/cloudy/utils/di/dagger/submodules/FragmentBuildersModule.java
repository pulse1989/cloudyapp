package za.co.kernelpanic.cloudy.utils.di.dagger.submodules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import za.co.kernelpanic.cloudy.ui.fragments.Forecast.WeatherFragment;

@Module
public  abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract WeatherFragment weatherFragment();
}
