package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.domain.City
import com.example.data.repository.WeatherRepository
import com.example.data.search.WeatherResult

class WeatherSearchViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val weatherRepository: WeatherRepository
): CoroutineViewModel(dispatchers) {

    private val _weatherLiveData = MutableLiveData<WeatherResult>()
    val weatherLiveData: LiveData<WeatherResult> = _weatherLiveData

    fun getWeatherFor(someCity: City) {
        weatherRepository.getCurrentWeather(someCity)
    }
}
