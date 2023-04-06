package com.example.data.search

import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.CityItem

sealed class SearchState {
    object ShowLoading : SearchState()
    object HideLoading : SearchState()
    object InvalidString : SearchState()
    data class CitiesResult(val results: Cities) : SearchState()
    data class ZipResult(val results:CityItem) : SearchState()
    data class Failure(val reason: String) : SearchState()
}
