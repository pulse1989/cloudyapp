package za.co.kernelpanic.cloudy.utils.di.dagger.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Application module - provides context for our app process
 */

@Module
public class AppModule {

    @Provides @Singleton
    Context providesContext(Application application){
        return application;
    }

}
