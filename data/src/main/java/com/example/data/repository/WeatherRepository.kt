package com.example.data.repository

import com.example.data.domain.City

interface WeatherRepository {
    fun getCurrentWeather(someCity: City)
}