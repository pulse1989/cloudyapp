package za.co.kernelpanic.cloudy.utils;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import za.co.kernelpanic.cloudy.utils.dagger.components.DaggerAppComponent;

public class App extends Application implements HasActivityInjector {

    /*
     * Our main activity class - this class is responsible for creating the required modules for use by our app.
     * This also where the android activity injector is defined before the activity is injected by dagger.
     */


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();


        DaggerAppComponent.builder().application(this).build().inject(this);

    }

    //crash error reporting


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}

