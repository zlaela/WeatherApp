package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.exception.HttpException
import com.example.data.search.SearchState

class SearchRepository(private val geoApi: GeoApi) {
    // City search can return 0 to 5 cities
    suspend fun searchCity(someCity: String): SearchState {
        return try {
            val result = geoApi.searchCityAsync(someCity).await()
            SearchState.Results(result)
        } catch (ex: HttpException) {
            SearchState.Failure("Failed")
        }
    }
    // Zip search returns 0 or 1 cities
    suspend fun searchZip(someZip: String): SearchState {
        return try {
            val result = geoApi.searchZipAsync(someZip).await()
            SearchState.Results(result)
        } catch (ex: HttpException) {
            SearchState.Failure("Failed")
        }
    }
}
