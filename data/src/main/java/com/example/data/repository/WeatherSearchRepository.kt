package com.example.data.repository

import com.example.data.api.WeatherApi
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
import com.example.data.domain.mapToForecast
import com.example.data.search.WeatherResult

class WeatherSearchRepository(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeather(someCity: City): WeatherResult {
        return runCatching {
            val result = weatherApi.getCurrentWeather(someCity.name).await()
            return WeatherResult.WeatherSuccess(result.mapToCurrentWeather())
        }.getOrElse { throwable ->
            WeatherResult.Failure(reasonFor(throwable))
        }
    }

    override suspend fun getForecast(someCity: City): WeatherResult {
        return runCatching {
            val result = weatherApi.getForecast(someCity.lat, someCity.lon).await()
            WeatherResult.ForecastSuccess(result.mapToForecast())
        }.getOrElse { throwable ->
            WeatherResult.Failure(reasonFor(throwable))
        }
    }
}