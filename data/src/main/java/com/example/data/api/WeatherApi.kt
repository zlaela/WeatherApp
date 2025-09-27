package com.example.data.api

import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String = "imperial",
    ): Deferred<CurrentWeatherResponse>

    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "imperial",
    ): Deferred<FiveDayForecastResponse>
}
