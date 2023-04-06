package com.example.data.api

interface GeoApi {
    fun searchZip(zip: String): List<String>
    fun searchCity(city: String): List<String>
}
