package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.domain.City
import com.example.data.repository.WeatherRepository
import com.example.data.search.WeatherResult
import kotlinx.coroutines.launch

class WeatherSearchViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val weatherRepository: WeatherRepository
) : CoroutineViewModel(dispatchers) {

    private val _weatherLiveData = MutableLiveData<WeatherResult>()
    val weatherLiveData: LiveData<WeatherResult> = _weatherLiveData

    fun getWeatherFor(someCity: City) {
        viewModelScope.launch {
            asyncSearch { weatherRepository.getCurrentWeather(someCity) }
        }
    }

    fun getForecast(someCity: City) {
        viewModelScope.launch {
            asyncSearch { weatherRepository.getForecast(someCity) }
        }
    }

    private fun asyncSearch(doAsync: suspend () -> WeatherResult) {
        _weatherLiveData.value = WeatherResult.ShowLoading
        launch(dispatchers.background) {
            // on background thread
            val result = doAsync()
            // on ui thread
            launch(dispatchers.ui) {
                _weatherLiveData.value = result
                _weatherLiveData.value = WeatherResult.HideLoading
            }
        }
    }
}
