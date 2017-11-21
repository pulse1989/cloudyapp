package za.co.kernelpanic.cloudy.utils.di.dagger.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import za.co.kernelpanic.cloudy.repository.remote.WeatherApi;

/** dependency injection for network dependencies **/

@Module
public class NetworkModule {

    @Provides @Singleton
    public CompositeDisposable providesCompositeDisposable(){

        return new CompositeDisposable();
    }

    @Provides @Singleton
        public OkHttpClient providesHttpInterceptor() {

          HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
          loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
          return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
      }

    @Provides @Singleton
        public Retrofit providesRetrofit() {
          return new Retrofit.Builder()
                     .baseUrl("http://api.openweathermap.org/data/2.5/forecast/")
                     .client(providesHttpInterceptor())
                     .addConverterFactory(GsonConverterFactory.create())
                     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                     .build();
      }

    @Provides @Singleton
    public WeatherApi providesWeatherApi(Retrofit retrofit) {
          return retrofit.create(WeatherApi.class);
    }

}
