package com.example.data.search

import com.example.data.domain.CurrentWeather
import com.example.data.domain.Forecast

sealed class WeatherResult {
    object ShowLoading : WeatherResult()
    object HideLoading : WeatherResult()
    data class WeatherSuccess(val locationWeather: CurrentWeather) : WeatherResult()
    data class ForecastSuccess(val locationForecast: List<Forecast>) : WeatherResult()
    data class Failure(val reason: String) : WeatherResult()
}