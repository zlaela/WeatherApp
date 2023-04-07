package com.example.data.api

import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.domain.City
import kotlinx.coroutines.Deferred

interface WeatherApi {
    suspend fun getCurrentWeather(city: City): Deferred<CurrentWeatherResponse>
}
