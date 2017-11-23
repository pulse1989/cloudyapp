package za.co.kernelpanic.cloudy.utils.di.dagger.modules;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Application module - provides any application specific dependencies.
 * Here we just need our LocationRequest object as we need it both within our app Activity and our Location utils class.
 */

@Module
public class AppModule {
     /*
     * Because we need to specify gps-specific permission requests and define location properties such as how often the gps should refresh,
     * what type of accuracy etc, we build out this dependency
     */
    @Provides @Singleton
    LocationRequest providesLocationRequest(){
        return new LocationRequest();
    }

    /*
     * Because we need a reference to our context
     */

    @Provides @Singleton
    Context providesContext(Application application){
        return application;
    }
}
