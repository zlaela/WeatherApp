package com.example.data.api.response.weather


import com.example.data.api.response.weather.details.City
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * API response containing 5 days of forecast data in 3 hour increments
 * @param [code] Internal parameter (2xx, 4xx, etc.)
 * @param [message] Internal parameter
 * @param [num_timestamps] A number of timestamps returned in the API response
 * @param [forecasts] 3-hour forecast entries
 * @param [city] Information about the city
 */
@JsonClass(generateAdapter = true)
data class FiveDayForecastResponse(
    @Json(name = "cod")
    val code: String?,
    @Json(name = "message")
    val message: Int?,
    @Json(name = "cnt")
    val num_timestamps: Int?,
    @Json(name = "list")
    val forecasts: List<Forecast3Hr>?,
    @Json(name = "city")
    val city: City?
)