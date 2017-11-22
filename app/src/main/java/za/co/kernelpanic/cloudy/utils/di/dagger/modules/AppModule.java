package za.co.kernelpanic.cloudy.utils.di.dagger.modules;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Application module - provides context for our app process
 */

@Module
public class AppModule {
     /*
     * Because we need to specify gps-specific permissions, we need to have this provided to our gps util class and the mainactivity
     */
    @Provides @Singleton
    LocationRequest providesLocationRequest(){
        return new LocationRequest();
    }

}
