package com.example.data.domain

import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse

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

fun FiveDayForecastResponse.mapToForecast(): List<Forecast> =
    with(mutableListOf<Forecast>()) {
        this@mapToForecast.forecasts.forEach {
            add(Forecast(
                it.dt,
                it.dtTxt,
                it.main.tempMin,
                it.main.tempMax,
                it.partOfDay.partOfDay,
                it.weather.first().icon,
                it.weather.first().main,
                it.weather.first().description
            ))
        }
        this
    }.toList()
