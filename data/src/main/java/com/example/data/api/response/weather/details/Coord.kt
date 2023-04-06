package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * City geo-location
 * @param lon = longitude
 * @param lat = latitude
 */
@JsonClass(generateAdapter = true)
data class Coord(
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "lat")
    val lat: Double
)