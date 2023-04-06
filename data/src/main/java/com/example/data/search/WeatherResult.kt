package com.example.data.search

import com.example.data.domain.CurrentWeather

sealed class WeatherResult {
    object ShowLoading : WeatherResult()
    object HideLoading : WeatherResult()
    data class Success(val locationWeather: CurrentWeather) : WeatherResult()

}