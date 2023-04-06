package com.example.data.api.response.weather.details


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param [rainLast3Hrs] Rain volume for last 3 hours, mm
 */
@JsonClass(generateAdapter = true)
data class Rain(
    @Json(name = "3h")
    val rainLast3Hrs: Double
)