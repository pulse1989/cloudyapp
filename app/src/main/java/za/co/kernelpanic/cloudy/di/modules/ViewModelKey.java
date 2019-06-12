package za.co.kernelpanic.cloudy.di.modules;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

/**
 * author: vusimoyo
 * created: 10:40 12/06/2019
 */
@MapKey
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelKey {
    Class <? extends ViewModel> value();
}

