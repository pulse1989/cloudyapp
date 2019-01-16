package za.co.kernelpanic.cloudy.data

import androidx.lifecycle.GeneratedAdapter
import com.squareup.moshi.JsonClass


@JsonClass (generateAdapter = true)
data class Forecast (val temperature: Temperature,
                     val weather : List<Weather>,
                     val pressure: Double,
                     val humidity: Double,
                     val windSpeed: Double,
                     val windDirection: Double)