package za.co.kernelpanic.cloudy.utils.di.dagger.components;


import android.app.Application;

import javax.inject.Singleton;


import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import za.co.kernelpanic.cloudy.utils.App;
import za.co.kernelpanic.cloudy.utils.di.dagger.modules.AppModule;
import za.co.kernelpanic.cloudy.utils.di.dagger.modules.NetworkModule;
import za.co.kernelpanic.cloudy.utils.di.dagger.modules.ViewModelModule;
import za.co.kernelpanic.cloudy.utils.di.dagger.modules.WeatherActivityModule;


@Singleton
@Component(modules = {AndroidInjectionModule.class, WeatherActivityModule.class, NetworkModule.class, AppModule.class, ViewModelModule.class})
public interface AppComponent {

@Component.Builder
 interface Builder {

            AppComponent build();

            @BindsInstance
            Builder application(Application application);
   }

    //where this will be injected
    void inject(App target);

}

