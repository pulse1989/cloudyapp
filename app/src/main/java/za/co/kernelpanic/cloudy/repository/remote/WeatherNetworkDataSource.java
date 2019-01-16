package za.co.kernelpanic.cloudy.repository.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.co.kernelpanic.cloudy.data.ForecastResponse;


/*
 * This class is responsible for going online to fetch our weather data and then returning the result to the LiveData ViewModel via the repository.
 * As with the gps location information, weather information is updated every 5 minutes once a new location has been sent to this class.
 * If the ViewModel's getWeather livedata doesn't have any subscribers, this class will effectively do nothing until it is needed.
 */
public class WeatherNetworkDataSource implements  WeatherNetworkInterface {

    private static final String LOG_TAG = WeatherNetworkDataSource.class.getSimpleName();

    private final WeatherApi weatherApi;

    private static final String API_KEY = "625546691b9f757f1da64b49d9740289";
    private static final String REQUEST_MODE = "json";
    private static final String REQUEST_UNITS = "metric";
    private static final int REQUEST_FORECAST_DAYS = 1;
    private MutableLiveData<ForecastResponse> weatherDataStream;
    private final Context context;

    /*
     * This is called from the repository class.
     * We use the sendRequest
     */

    @Inject
    public WeatherNetworkDataSource(Context context, WeatherApi weatherApi){

        this.weatherApi = weatherApi;
        this.context = context;
        this.weatherDataStream = new MutableLiveData<>();
    }

    /*
     * Responsible for getting data directly from the API and returning the LiveData  to the repository
     */

    @Override
    public LiveData<ForecastResponse> fetchWeatherForecast(double latitude, double longitude) {


        Call<ForecastResponse> apiResponse = weatherApi.getForecast(API_KEY, latitude, longitude, REQUEST_MODE, REQUEST_UNITS, REQUEST_FORECAST_DAYS);
        apiResponse.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {

                if(response.isSuccessful()) {

                    Log.i(LOG_TAG, "We managed to get data!");

                    weatherDataStream.setValue(response.body());

                } else {

                    Log.e(LOG_TAG, "Check the service status or the server you're pointing to. Probably something invalid");

                    switch(response.code()) {

                        case 404:
                            Toast.makeText(context, "The weather for your location could not be found right now. Lets try again later", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(context, "The weather service is unavailable right now. Let's try again later", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "An unknown error has occurred", Toast.LENGTH_LONG).show();
                            break;

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {

                Log.e(LOG_TAG, "Something went horribly wrong: \n" + t.getMessage());
                Toast.makeText(context, "Something went wrong. Check your connection and try again", Toast.LENGTH_LONG).show();

            }
        });

        return weatherDataStream;
    }

}
