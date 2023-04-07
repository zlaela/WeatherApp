package com.example.data.repository

import com.example.data.api.WeatherApi
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
import com.example.data.exception.HttpException
import com.example.data.search.WeatherResult

class WeatherSearchRepository(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeather(someCity: City): WeatherResult {
        return try {
            val resultAsync = weatherApi.getCurrentWeather(someCity).await()
            WeatherResult.Success(resultAsync.mapToCurrentWeather())
        } catch (ex: HttpException) {
            WeatherResult.Failure("Bad")
        }
    }

    override suspend fun getForecast(someCity: City): WeatherResult {
        TODO("Not yet implemented")
    }
}