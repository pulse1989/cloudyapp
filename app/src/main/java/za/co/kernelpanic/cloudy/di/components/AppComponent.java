package za.co.kernelpanic.cloudy.di.components;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import za.co.kernelpanic.cloudy.utils.App;
import za.co.kernelpanic.cloudy.di.modules.AppModule;
import za.co.kernelpanic.cloudy.di.modules.NetworkModule;
import za.co.kernelpanic.cloudy.di.modules.ViewModelModule;
import za.co.kernelpanic.cloudy.di.modules.WeatherActivityModule;

/**
 * Our app component is responsible for building out our object graph so that dagger is aware of each and every object instance to provide when needed.
 * This is why we pass all our modules here. Two new ones Though
 * AndroidInjectionModule -
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, WeatherActivityModule.class, NetworkModule.class, AppModule.class, ViewModelModule.class})
public interface AppComponent {

    Application myapp();

    /*
     * We need to bind our application scope instance to the dagger graph. this will help with application context anywhere in the app.
     * So we declare a builder  and bind the instance.
     */
    @Component.Builder
 interface Builder {

            @BindsInstance
            Builder application(Application application);

            AppComponent build();

   }

    //where this will be injected
    void inject(App target);

}

