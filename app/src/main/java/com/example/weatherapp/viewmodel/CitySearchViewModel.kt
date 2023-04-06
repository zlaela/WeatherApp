package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.search.SearchState
import com.example.weatherapp.validation.StringValidator
import com.example.weatherapp.validation.ValidationResult

class CitySearchViewModel(
    private val repository: com.example.data.repository.SearchRepository,
    private val validator: StringValidator
) {
    private val _liveData = MutableLiveData<SearchState>()
    val searchLiveData: LiveData<SearchState> = _liveData

    fun search(userInput: String) {
        _liveData.value = SearchState.ShowLoading
        _liveData.value = when (validator.validate(userInput)) {
            is ValidationResult.ValidCity,
            is ValidationResult.ValidZip -> repository.search(userInput)
            is ValidationResult.Invalid -> SearchState.InvalidString
        }
        _liveData.value = SearchState.HideLoading
    }
}
