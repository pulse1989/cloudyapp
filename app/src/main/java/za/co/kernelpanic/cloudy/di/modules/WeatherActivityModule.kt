package za.co.kernelpanic.cloudy.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import za.co.kernelpanic.cloudy.ui.activities.WeatherActivity
import za.co.kernelpanic.cloudy.di.submodules.FragmentBuildersModule

@Module
abstract class WeatherActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesWeatherActivity(): WeatherActivity
}
