package com.example.data.api.response.city

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class Cities : ArrayList<CityItem>()

@JsonClass(generateAdapter = true)
data class CityItem(
    @Json(name = "name")
    val name: String,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "local_names")
    val localNames: LocalNames? = null
)