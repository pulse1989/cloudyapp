package za.co.kernelpanic.cloudy.repository.remote;

import android.arch.lifecycle.LiveData;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import za.co.kernelpanic.cloudy.data.Forecast;
import za.co.kernelpanic.cloudy.data.ForecastResponse;

/**
 * Interface responsible for making requests to our weather Api service.
 * Our queries contain our latitude and longitude as received by our location listener service.
 **/

public interface WeatherApi {

    // get the 7 day forecast
    @GET("daily")
    Single<ForecastResponse> getForecast(
            @Query("APPID") String appId,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("mode") String mode,
            @Query("units") String unitType,
            @Query("cnt") int dayCount);
}
