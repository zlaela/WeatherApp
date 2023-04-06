package com.example.data.api.response.weather

import com.example.data.api.response.weather.details.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/* API response for requesting weather data for a location
 * Query with only the city name defaults to the US
 * @param [coord] City geo location
 * @param [weather] more info Weather condition codes
 * @param [base] Internal parameter
 * @param [main] Components of forecast
 * @param [visibility] Average visibility, metres. The maximum value of the visibility is 10km
 * @param [wind] wind conditions
 * @param [clouds] % cloudiness
 * @param [dt] Time of data calculation, unix, UTC
 * @param [sys] Sunrise/sunset/country code information
 * @param [timezone] Shift in seconds from UTC
 * @param [id] City ID. Please note that built-in geocoder functionality has been deprecated.
 * @param [name] City name. Please note that built-in geocoder functionality has been deprecated.
 * @param [code] Internal parameter (2xx, 4xx, etc.)
 */
@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @Json(name = "coord")
    val coord: Coord,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "base")
    val base: String,
    @Json(name = "main")
    val main: Main,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "wind")
    val wind: Wind,
    @Json(name = "clouds")
    val clouds: Clouds,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "cod")
    val code: Int
)