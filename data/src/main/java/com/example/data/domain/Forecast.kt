package com.example.data.domain

data class Forecast(
    val forecastDateTimeUtc: Int,
    val forecastDateTime: String,
    val tempMin: Double,
    val tempMax: Double,
    val partOfDay: String,
    val icon: String,
    val condition: String,
    val description: String
)