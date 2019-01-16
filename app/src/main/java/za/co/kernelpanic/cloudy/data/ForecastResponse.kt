package za.co.kernelpanic.cloudy.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class ForecastResponse (val city: City,
                             @Json(name = "list")val forecastList: List<Forecast>)