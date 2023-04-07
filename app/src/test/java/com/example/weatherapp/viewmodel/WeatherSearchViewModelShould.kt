package com.example.weatherapp.viewmodel

import com.example.data.domain.City
import com.example.data.repository.WeatherRepository
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.TestCoroutineDispatchers
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class WeatherSearchViewModelShould {
    @RelaxedMockK
    private lateinit var weatherRepository: WeatherRepository

    private lateinit var weatherViewModel: WeatherSearchViewModel

    private val someCity: City = City("Chicago", 41.8755616, -87.6244212)

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        weatherViewModel = WeatherSearchViewModel(dispatchers, weatherRepository)
    }

    @Test
    fun `perform weather search`() {
        // When getWeatherFor() is called in the view model
        weatherViewModel.getWeatherFor(someCity)

        // The repository performs a search for the given city
        coVerify (exactly = 1) { weatherRepository.getCurrentWeather(someCity) }
    }
}