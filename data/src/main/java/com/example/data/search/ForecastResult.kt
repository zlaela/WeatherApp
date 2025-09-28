package com.example.data.search

import com.example.data.domain.DayNightForecast

sealed class ForecastResult {
    object Initial : ForecastResult()
    data class Loading(val isLoading: Boolean) : ForecastResult()
    data class ForecastSuccess(val locationForecast: List<DayNightForecast>) : ForecastResult()
    data class Failure(val reason: String) : ForecastResult()
}