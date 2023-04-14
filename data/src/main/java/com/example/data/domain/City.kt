package com.example.data.domain

data class City (
    val country: String,
    val state: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val id: Int? = null
)