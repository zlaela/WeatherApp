package com.example.data.api

import kotlinx.coroutines.Deferred

interface GeoApi {
    suspend fun searchZipAsync(zip: String): Deferred<List<String>>
    suspend fun searchCityAsync(city: String): Deferred<List<String>>
}
