package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.search.SearchState

class SearchRepository(private val geoApi: GeoApi) {
    fun search(someCity: String): SearchState {
        val result = geoApi.searchCity(someCity)
        return SearchState.Results(result)
    }
}
