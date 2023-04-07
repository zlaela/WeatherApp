package com.example.data.domain

import com.example.data.api.response.weather.CurrentWeatherResponse

fun CurrentWeatherResponse.mapToCurrentWeather(): CurrentWeather =
    CurrentWeather(
        this.name,
        this.coord.lat,
        this.coord.lon,
        this.main.tempMin,
        this.main.tempMax,
        this.main.temp,
        this.main.feelsLike,
        this.weather.first().icon,
        this.weather.first().description
    )
