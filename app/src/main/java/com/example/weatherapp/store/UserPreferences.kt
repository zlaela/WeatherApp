package com.example.weatherapp.store

import com.example.data.domain.City

data class UserPreferences(
    val city: City,
    val country: String,
)
