package za.co.kernelpanic.cloudy.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;


/**
 * This is where all our little viewmodels come from :)
 * We only need one instance of our factory. This is where any ViewModel's will be created as long as the viewmodel is provided within the ViewModelModule
 */
@Singleton
public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    /*
     * What we're doing here is building up instances of the viewmodels we've created for our UI.
     * This map consists of both the class that is to be the key (mandatory has to extend ViewModel), as well as the actual viewmodel.
     * What Provider<ViewModel> will do here is provide an instance of the class that's needed without the developer needing to create it manually  it continuously.
     *
     * What's great about this is that together with the ViewModelModule where you declare all our ViewModel instances, you can use this factory to implement each
     * ViewModel when it's needed throughout the application.
     */
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public WeatherViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Provider<?extends ViewModel> creator= creators.get(modelClass); // checks to see if there are any existing viewmodel classes  here

        if(creator == null) { // if there are no viewmodels

            for(Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry: creators.entrySet()) { //create instances of classes added to the map.

                if(modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }

        if (creator == null) {
            throw  new IllegalArgumentException("unknown model class" + modelClass);

        } try {

            return (T) creator.get();

        } catch (Exception e) {

            throw  new RuntimeException(e);
        }
    }
}
