package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Weather condition codes
 * @param id = Weather condition ID
 * @param main = Group of weather parameters (rain, snow, extreme, etc.)
 * @param description = Weather condition within the group (can be localized)
 * @param icon = Weather icon id
 */
@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "main")
    val main: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "icon")
    val icon: String?
)