package com.example.data.api.response.weather.details


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param [snowLast3Hrs] Snow volume for last 3 hours, mm
 */
@JsonClass(generateAdapter = true)
data class Snow(
    @Json(name = "3h")
    val snowLast3Hrs: Double?
)