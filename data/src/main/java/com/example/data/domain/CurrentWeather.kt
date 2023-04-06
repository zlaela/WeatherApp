package com.example.data.domain

/**
 * Weather object representing what the UI should display for the current weather
 */
data class CurrentWeather(
    val city: String,
    val lat: Double,
    val lon: Double,
    val tempMin: Double,
    val tempMax: Double,
    val temp: Double,
    val feelsLike: Double,
    val icon: String,
    val description: String
)

