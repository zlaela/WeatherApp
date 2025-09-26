package com.example.data.repository

import com.example.data.api.WeatherApi
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
import com.example.data.domain.mapToForecast
import com.example.data.search.WeatherResult
import retrofit2.HttpException
import java.net.UnknownHostException

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

    fun reasonFor(throwable: Throwable) = when (throwable) {
        is UnknownHostException ->  "Network error"
        is HttpException -> "HTTP error ${throwable.code()}"
        else -> "Network error"
    }
}