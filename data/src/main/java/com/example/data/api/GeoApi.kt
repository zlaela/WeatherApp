package com.example.data.api

import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.CityItem
import kotlinx.coroutines.Deferred

interface GeoApi {
    suspend fun searchZipAsync(zip: String): Deferred<CityItem>
    suspend fun searchCityAsync(city: String): Deferred<Cities>
}
