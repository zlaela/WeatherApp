package com.example.data.search

sealed class SearchState {
    object ShowLoading: SearchState()
    object HideLoading: SearchState()
    object InvalidString: SearchState()
    data class Results(val results: List<String>): SearchState()
    data class Failure(val reason: String) : SearchState()
}
