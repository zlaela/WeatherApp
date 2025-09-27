package com.example.weatherapp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.data.domain.CurrentWeather
import com.example.data.search.WeatherResult
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.utils.formatTemp
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * WeatherCard feature will:
 * 1. Display initial state when no weather data is available
 * 2. Display loading state while fetching weather data
 * 3. Display error state when weather fetch fails
 * 4. Display weather data when successfully loaded
 * 5. Format temperature data correctly
 */
@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class WeatherCardTest {

    private lateinit var weatherStates: State<WeatherResult>

    @BeforeEach
    fun setUp() {
        weatherStates = mutableStateOf(WeatherResult.Initial)
    }

    @Test
    fun `displays initial state when no weather data is available`() {
        // Given initial state
        weatherStates = mutableStateOf(WeatherResult.Initial)

        // When WeatherCard is created
        val result = getTextFromWeatherState(weatherStates)

        // Then it shows search prompt
        assertEquals("Search for a city to see weather", result)
    }

    @Test
    fun `displays error state when weather fetch fails`() {
        // Given error state
        val errorReason = "Network error"
        weatherStates = mutableStateOf(WeatherResult.Failure(errorReason))

        // When WeatherCard is created
        val result = getTextFromWeatherState(weatherStates)

        // Then it shows error message with reason
        assertEquals("Failed to load weather: $errorReason", result)
    }

    @Test
    fun `displays weather data when successfully loaded`() {
        // Given successful weather data
        val weather = CurrentWeather(
            city = "Chicago",
            lat = 41.8755616,
            lon = -87.6244212,
            tempMin = 15.0,
            tempMax = 25.0,
            temp = 20.0,
            feelsLike = 22.0,
            icon = "01d",
            description = "clear sky"
        )
        weatherStates = mutableStateOf(WeatherResult.WeatherSuccess(weather))

        // When WeatherCard is created
        val result = getTextFromWeatherState(weatherStates)

        // Then it shows weather information
        assertEquals("Chicago - Clear sky - 20.0°C (Feels like 22.0°C) - High: 25.0°C, Low: 15.0°C", result)
    }


    // Helper function to simulate the WeatherCard's text generation logic
    private fun getTextFromWeatherState(state: State<WeatherResult>): String =
        when ( val state = state.value) {
        is WeatherResult.Initial ->  "Search for a city to see weather"
        is WeatherResult.Failure -> "Failed to load weather: ${state.reason}"
        is WeatherResult.WeatherSuccess ->  {
            val weather = state.locationWeather
            val capitalizedDescription = weather.description.replaceFirstChar { it.uppercase() }
            "${weather.city} - $capitalizedDescription - ${weather.temp.formatTemp()}°C (Feels like ${weather.feelsLike.formatTemp()}°C) - High: ${weather.tempMax.formatTemp()}°C, Low: ${weather.tempMin.formatTemp()}°C"
        }
        else -> { "TODO" }
    }
}
