package za.co.kernelpanic.cloudy.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Temperature(@Json(name = "dt") val date: Long? = 0,
                       @Json(name = "day") val dayForecast: Double? = 0.0,
                       @Json(name = "min") val minTemp: Double? = 0.0,
                       @Json(name = "max") val maxTemp: Double? = 0.0)