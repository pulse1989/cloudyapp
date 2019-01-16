package za.co.kernelpanic.cloudy.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Weather (val main: String? = "",
                    val description: String? = "",
                    @Json(name = "id") val iconId: Int? = 0)