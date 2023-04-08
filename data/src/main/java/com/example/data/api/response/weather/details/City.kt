package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param [id] City ID. Please note that built-in geocoder functionality has been deprecated. Learn more here.
 * @param [name] City name. Please note that built-in geocoder functionality has been deprecated. Learn more here.
 * @param [coord]
 * @param [coord].lat City geo location, latitude
 * @param [coord].lon City geo location, longitude
 * @param [country] Country code (GB, JP etc.). Please note that built-in geocoder functionality has been deprecated. Learn more here.
 * @param [population] City population
 * @param [timezone] Shift in seconds from UTC
 * @param [sunrise] Sunrise time, Unix, UTC
 * @param [sunset] Sunset time, Unix, UTC
 */

@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "coord")
    val coord: Coord?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "population")
    val population: Int?,
    @Json(name = "timezone")
    val timezone: Int?,
    @Json(name = "sunrise")
    val sunrise: Int?,
    @Json(name = "sunset")
    val sunset: Int?
)