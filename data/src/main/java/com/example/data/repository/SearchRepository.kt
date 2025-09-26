package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.domain.mapToCitiesList
import com.example.data.domain.mapToCity
import com.example.data.search.SearchState

class SearchRepository(private val geoApi: GeoApi) {
    // City search can return 0 to 5 cities
    suspend fun searchCity(someCity: String): SearchState {
        return try {
            val result = geoApi.searchCityAsync(someCity).await().mapToCitiesList()
            SearchState.CitiesResult(result)
        } catch (ex: retrofit2.HttpException) {
            SearchState.Failure("Failed")
        }
    }

    // Zip search returns 0 or 1 cities
    suspend fun searchZip(someZip: String): SearchState {
        return try {
            val result = geoApi.searchZipAsync(someZip).await().mapToCity()
            SearchState.ZipResult(result)
        } catch (ex: retrofit2.HttpException) {
            SearchState.Failure("Failed")
        }
    }
}
