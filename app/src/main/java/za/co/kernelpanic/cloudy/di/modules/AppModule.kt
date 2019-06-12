package za.co.kernelpanic.cloudy.di.modules

import android.app.Application
import android.content.Context

import com.google.android.gms.location.LocationRequest

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Application module - provides any application specific dependencies.
 * Here we just need our LocationRequest object as we need it both within our app Activity and our Location utils class.
 */
@Module
class AppModule {
    /*
     * Because we need to specify gps-specific permission requests and define location properties such as how often the gps should refresh,
     * what type of accuracy etc, we build out this dependency
     */
    @Provides
    @Singleton
    internal fun providesLocationRequest(): LocationRequest {
        return LocationRequest()
    }

    /*
     * Because we need a reference to our context
     */
    @Provides
    @Singleton
    internal fun providesContext(application: Application): Context {
        return application
    }
}
