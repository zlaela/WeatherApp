package com.example.data.api.response.weather.details


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param [partOfDay] Part of the day (n - night, d - day)
 */
@JsonClass(generateAdapter = true)
data class PartOfDay(
    @Json(name = "pod")
    val partOfDay: String
)