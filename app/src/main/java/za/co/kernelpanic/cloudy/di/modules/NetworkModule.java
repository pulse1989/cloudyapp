package za.co.kernelpanic.cloudy.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import za.co.kernelpanic.cloudy.repository.remote.WeatherApi;

/**
 * The network module is where all things network are declared
 * We declare retrofit, our interceptors and create our retrofit instance for use within the app.
 */

@Module
public class NetworkModule {

    /*
     * We need to make sure our http requests are not experiencing any issues such as invalid API keys or bad HTTP error codes.
     * So we build an interceptor in order to make sure that all requests are captured (both the GET and the Response).
     * Really awesome debugging tool. The output very much matches a normal cURL command.
     * We build this up using dagger and provide it to retrofit
     */

    private String getBaseUrl() {
        return "https://api.openweathermap.org/data/2.5/forecast/";
    }

    @Provides
    @Singleton
    public OkHttpClient providesHttpInterceptor() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Dispatcher requestDispatcher = new Dispatcher();
        requestDispatcher.setMaxRequests(1); // we ned to rate limit the amount of requests in parallel to one. This way we don't flood the network (and our app)
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).dispatcher(requestDispatcher).build();
    }

    /*
     * Retrofit will be doing the API duties here. We feed it the baseURL we need it to fetch, give it an okHttp3 client in the form of our interceptor
     * the Gson Factory will be used to convert the JSON data into our java POJO's
     */
    @Provides
    @Singleton
    public Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(providesHttpInterceptor())
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    /*
     *  We build up the retrofit client using our weatherApi class that containts all the methods we need retrofit to manage.
     */
    @Provides
    @Singleton
    public WeatherApi providesWeatherApi(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }

}
