package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SearchRepository
import com.example.data.search.SearchState
import com.example.weatherapp.validation.StringValidator
import com.example.weatherapp.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val repository: SearchRepository,
    private val validator: StringValidator,
) : CoroutineViewModel(dispatchers) {
    private val _liveData = MutableLiveData<SearchState>()
    val searchLiveData: LiveData<SearchState> = _liveData

    fun search(userInput: String) {
        when (validator.validate(userInput)) {
            is ValidationResult.ValidCity ->
                viewModelScope.launch { asyncSearch { repository.searchCity(userInput) } }
            is ValidationResult.ValidZip ->
                viewModelScope.launch { asyncSearch { repository.searchZip(userInput) } }
            is ValidationResult.Invalid -> _liveData.value = SearchState.InvalidString
        }
    }

    private fun asyncSearch(doAsync: suspend () -> SearchState) {
        _liveData.value = SearchState.ShowLoading
        launch(dispatchers.background) {
            // on background thread
            val result = doAsync()
            // on ui thread
            launch(dispatchers.ui) {
                _liveData.value = result
                _liveData.value = SearchState.HideLoading
            }
        }
    }
}
