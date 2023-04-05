package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.search.SearchState
import com.example.weatherapp.validation.StringValidator

class CitySearchViewModel(
    private val validator: StringValidator
) {
    private val _liveData = MutableLiveData<SearchState>()
    val searchLiveData: LiveData<SearchState> = _liveData

    fun search(userInput: String) {
        validator.validate(userInput)
    }
}
