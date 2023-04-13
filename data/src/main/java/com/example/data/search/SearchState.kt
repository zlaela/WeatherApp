package com.example.data.search

import com.example.data.domain.City

sealed class SearchState {
    object ShowLoading : SearchState()
    object HideLoading : SearchState()
    object InvalidString : SearchState()
    data class CitiesResult(val results: List<City>) : SearchState()
    data class ZipResult(val results: City) : SearchState()
    data class Failure(val reason: String) : SearchState()
}
