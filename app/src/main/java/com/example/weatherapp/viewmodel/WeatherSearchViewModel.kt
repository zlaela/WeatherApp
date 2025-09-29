package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.data.domain.City
import com.example.data.repository.DataStoreRepository
import com.example.data.repository.WeatherRepository
import com.example.data.search.ForecastResult
import com.example.data.search.WeatherResult
import com.example.data.store.DataStoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherSearchViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val weatherRepository: WeatherRepository,
    private val dataStoreRepository: DataStoreRepository,
) : CoroutineViewModel(dispatchers) {

    private val _forecastLivedata = MutableLiveData<ForecastResult>()
    val forecastLivedata: LiveData<ForecastResult> = _forecastLivedata

    private val _weatherLiveData = MutableLiveData<WeatherResult>()
    val weatherLiveData: LiveData<WeatherResult> = _weatherLiveData

    val getPrefsOnStart: LiveData<DataStoreState> = liveData {
        emit(dataStoreRepository.getPreferences())
    }

    init {
        getWeatherAndForecastFromPrefs()
    }

    fun getWeatherFor(someCity: City) {
        viewModelScope.launch {
            searchWeather {
                updatePrefs(someCity)
                weatherRepository.getCurrentWeather(someCity)
            }
        }
    }

    fun getForecast(someCity: City) {
        viewModelScope.launch {
            searchForecast {
                updatePrefs(someCity)
                weatherRepository.getForecast(someCity)
            }
        }
    }

    fun refreshWeatherAndForecast() {
        viewModelScope.launch {
            dataStoreRepository.getStoredCity()?.let { city ->
                getWeatherFor(city)
                getForecast(city)
            }
        }
    }

    private fun getWeatherAndForecastFromPrefs() {
        viewModelScope.launch {
            dataStoreRepository.getStoredCity()?.let {
                searchWeather {
                    weatherRepository.getCurrentWeather(it)
                }
                searchForecast {
                    weatherRepository.getForecast(it)
                }
            }
        }
    }

    private fun searchForecast(doAsync: suspend () -> ForecastResult) {
        _forecastLivedata.value = ForecastResult.Loading(true)
        launch(dispatchers.background + exceptionContext) {
            // on background thread
            val result = doAsync()
            // on ui thread
            launch(dispatchers.ui) {
                _forecastLivedata.value = ForecastResult.Loading(false)
                _forecastLivedata.value = result
            }
        }
    }

    private fun searchWeather(doAsync: suspend () -> WeatherResult) {
        _weatherLiveData.value = WeatherResult.Loading(true)
        launch(dispatchers.background + exceptionContext) {
            // on background thread
            val result = doAsync()
            // on ui thread
            launch(dispatchers.ui) {
                _weatherLiveData.value = WeatherResult.Loading(false)
                _weatherLiveData.value = result
            }
        }
    }

    private suspend fun updatePrefs(city: City) {
        launch(dispatchers.background) {
            dataStoreRepository.updateCity(city)
            if (city.country.isNotBlank()) {
                dataStoreRepository.updateCountry(city.country)
            }
        }
    }
}
