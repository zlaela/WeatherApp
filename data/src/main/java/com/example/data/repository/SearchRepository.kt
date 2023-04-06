package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.exception.HttpException
import com.example.data.search.SearchState

class SearchRepository(private val geoApi: GeoApi) {
    suspend fun search(someCity: String): SearchState {
        return try {
            val result = geoApi.searchCityAsync(someCity).await()
            SearchState.Results(result)
        } catch (ex: HttpException) {
            SearchState.Failure("Failed")
        }
    }
}
