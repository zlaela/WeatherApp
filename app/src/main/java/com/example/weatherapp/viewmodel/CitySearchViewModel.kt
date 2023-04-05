package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.search.SearchState
import com.example.weatherapp.validation.StringValidator

class CitySearchViewModel(
    private val repository: com.example.data.repository.SearchRepository,
    private val validator: StringValidator
) {
    private val _liveData = MutableLiveData<SearchState>()
    val searchLiveData: LiveData<SearchState> = _liveData

    fun search(userInput: String) {
        if (validator.validate(userInput)) {
            repository.search(userInput)
        } else {
            _liveData.value = SearchState.InvalidString
        }
    }
}
