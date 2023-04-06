package com.example.data.api

import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.City
import kotlinx.coroutines.Deferred

interface GeoApi {
    suspend fun searchZipAsync(zip: String): Deferred<City>
    suspend fun searchCityAsync(city: String): Deferred<Cities>
}
