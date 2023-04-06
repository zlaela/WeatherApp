package com.example.data.api

import com.example.data.api.response.city.City
import kotlinx.coroutines.Deferred

interface GeoApi {
    suspend fun searchZipAsync(zip: String): Deferred<List<City>>
    suspend fun searchCityAsync(city: String): Deferred<List<City>>
}
