package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * @param [speed] Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
 * @param [deg] Wind direction, degrees (meteorological)
 * @param [gust] Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
 */

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed")
    val speed: Double?,
    @Json(name = "deg")
    val deg: Int?,
    @Json(name = "gust")
    val gust: Double?
)