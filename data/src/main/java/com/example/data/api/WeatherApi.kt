package com.example.data.api

import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse
import com.example.data.domain.City
import kotlinx.coroutines.Deferred

interface WeatherApi {
    suspend fun getCurrentWeather(city: City): Deferred<CurrentWeatherResponse>
    suspend fun getForecast(city: City): Deferred<FiveDayForecastResponse>
}
