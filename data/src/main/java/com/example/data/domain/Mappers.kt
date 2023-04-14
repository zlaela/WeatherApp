package com.example.data.domain

import com.example.data.api.response.city.CityItem
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse

fun List<CityItem>.mapToCitiesList(): List<City> =
    this.map {
        it.mapToCity()
    }

fun CityItem.mapToCity(): City {
    this.run {
        println("City item is $this")
        return if (name == null) {
            City("", "", "Unknown City", 0.0, 0.0)
        } else {
            City(country ?: "", state ?: "", name, lat ?: 0.0, lon ?: 0.0)
        }
    }
}

fun CurrentWeatherResponse.mapToCurrentWeather(): CurrentWeather =
    CurrentWeather(
        this.name ?: "",
        this.coord?.lat ?: 0.0,
        this.coord?.lon ?: 0.0,
        this.main?.tempMin ?: 0.0,
        this.main?.tempMax ?: 0.0,
        this.main?.temp ?: 0.0,
        this.main?.feelsLike ?: 0.0,
        this.weather?.first()?.icon ?: "",
        this.weather?.first()?.description ?: ""
    )

fun FiveDayForecastResponse.mapToForecast(): List<Forecast> =
    with(mutableListOf<Forecast>()) {
        this@mapToForecast.forecasts?.forEach {
            add(Forecast(
                it.dt ?: 0,
                it.dtTxt ?: "",
                it.main?.tempMin ?: 0.0,
                it.main?.tempMax ?: 0.0,
                it.partOfDay?.partOfDay ?: "",
                it.weather?.first()?.icon ?: "",
                it.weather?.first()?.main ?: "",
                it.weather?.first()?.description ?: ""
            ))
        }
        this
    }.toList()
