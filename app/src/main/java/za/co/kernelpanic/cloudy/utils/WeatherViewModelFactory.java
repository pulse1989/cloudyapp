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
 */
@Singleton
public class WeatherViewModelFactory implements ViewModelProvider.Factory {

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

            for(Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry: creators.entrySet()) { //creat

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
