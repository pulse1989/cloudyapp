package za.co.kernelpanic.cloudy.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass (generateAdapter = true)
data class Forecast (@Json(name = "temp" )val temperature: Temperature,
                     val weather : List<Weather>,
                     val pressure: Double,
                     val humidity: Double,
                     @Json(name = "speed") val windSpeed: Double,
                     @Json(name = "deg") val windDirection: Double)