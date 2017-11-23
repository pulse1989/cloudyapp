package za.co.kernelpanic.cloudy.utils.dagger.modules;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

/**
 * A custom annotation that we use in order to annotate our ViewModels as they are added into a Map by dagger
 * the value that goes with this key will be the actual viewmodel - of which dagger will return an instance of, when needed.
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
@interface ViewModelKey {
    Class<? extends ViewModel> value();
}

