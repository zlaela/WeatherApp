package com.example.data.api.response.weather

import com.example.data.api.response.weather.details.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param [dt] Time of data forecasted, unix, UTC
 * @param [main] Components of forecast
 * @param [weather] Weather conditions
 * @param [clouds] % cloudiness
 * @param [wind] wind conditions
 * @param [visibility] Average visibility, metres. The maximum value of the visibility is 10km
 * @param [probabilityOfPrecip] Probability of precipitation. The values of the parameter vary between 0 and 1, where 0 is equal to 0%, 1 is equal to 100%
 * @param [partOfDay] Part of day
 * @param [dtTxt] Time of data forecasted, ISO, UTC
 * @param [rain] Rain info for the last 3 hours
 * @param [snow] Snow info for the last 3 hours
 */
@JsonClass(generateAdapter = true)
data class Forecast3Hr(
    @Json(name = "dt")
    val dt: Int?,
    @Json(name = "main")
    val main: Main?,
    @Json(name = "weather")
    val weather: List<Weather>?,
    @Json(name = "clouds")
    val clouds: Clouds? = null,
    @Json(name = "wind")
    val wind: Wind? = null,
    @Json(name = "visibility")
    val visibility: Int?,
    @Json(name = "pop")
    val probabilityOfPrecip: Double?,
    @Json(name = "sys")
    val partOfDay: PartOfDay?,
    @Json(name = "dt_txt")
    val dtTxt: String?,
    @Json(name = "rain")
    val rain: Rain? = null,
    @Json(name = "snow")
    val snow: Snow? = null
)