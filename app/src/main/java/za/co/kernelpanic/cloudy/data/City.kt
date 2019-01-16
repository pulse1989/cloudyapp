package za.co.kernelpanic.cloudy.data

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class City(val name: String)