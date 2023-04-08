package com.example.data.api.response.weather.details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
/**
 * Main components of the current weather forecast
 * @param [temp] Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * @param [feelsLike] This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * @param [tempMin] Minimum temperature at the moment of calculation. This is minimal forecasted temperature (within large megalopolises and urban areas), use this parameter optionally. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * @param [tempMax] Maximum temperature at the moment of calculation. This is maximal forecasted temperature (within large megalopolises and urban areas), use this parameter optionally. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 * @param [pressure] Atmospheric pressure on the sea level by default, hPa
 * @param [pressureSeaLevel] Atmospheric pressure on the sea level, hPa
 * @param [pressureGroundLevel] Atmospheric pressure on the ground level, hPa
 * @param [humidity] Humidity, %
 * @param [tempKf] Internal parameter
 */
@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp")
    val temp: Double?,
    @Json(name = "feels_like")
    val feelsLike: Double?,
    @Json(name = "temp_min")
    val tempMin: Double?,
    @Json(name = "temp_max")
    val tempMax: Double?,
    @Json(name = "pressure")
    val pressure: Int?,
    @Json(name = "sea_level")
    val pressureSeaLevel: Int? = null,
    @Json(name = "grnd_level")
    val pressureGroundLevel: Int? = null,
    @Json(name = "humidity")
    val humidity: Int?,
    @Json(name = "temp_kf")
    val tempKf: Double? = null
)