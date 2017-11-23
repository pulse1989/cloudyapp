package za.co.kernelpanic.cloudy.repository.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import za.co.kernelpanic.cloudy.data.ForecastResponse;

/**
 * Interface responsible for making requests to our weather Api service.
 * Our queries contain our latitude and longitude as received by our location listener service.
 **/

public interface WeatherApi {

    /*
     * This is the actual api call performed by retrofit.
     * We pass in all the queries we want, including user location in
     * latitude and longitude. Because we're biased, we'll only be grabbing metric units.
     * This can easily be changed though, which is why it's a query rather than a hard-coded value.
     * Same as the rest
     */
    @GET("daily")
    Call<ForecastResponse> getForecast(
            @Query("APPID") String appId,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("mode") String mode,
            @Query("units") String unitType,
            @Query("cnt") int dayCount);
}
