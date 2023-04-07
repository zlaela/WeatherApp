package com.example.data.repository

import com.example.data.domain.City
import com.example.data.search.WeatherResult

interface WeatherRepository {
    suspend fun getCurrentWeather(someCity: City): WeatherResult
    suspend fun getForecast(someCity: City): WeatherResult
}