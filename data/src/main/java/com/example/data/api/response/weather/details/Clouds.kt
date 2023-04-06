package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Cloudiness, %
 */
@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all")
    val all: Int
)