package com.example.data.domain

import java.time.LocalDate

// Represents one day's forecast, split into day and night
data class DayNightForecast(
    val localDate: LocalDate,
    val dateString: String,
    val dayForecast: Forecast?,
    val nightForecast: Forecast?,
    val dayHigh: Double,
    val dayLow: Double,
    val nightHigh: Double,
    val nightLow: Double
)