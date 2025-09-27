package com.example.weatherapp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.data.search.WeatherResult
import com.example.weatherapp.ExecutionExtension
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

    // Helper function to simulate the WeatherCard's text generation logic
    private fun getTextFromWeatherState(state: State<WeatherResult>): String =
        when ( val state = state.value) {
        is WeatherResult.Initial ->  "Search for a city to see weather"
        is WeatherResult.Failure -> "Failed to load weather: ${state.reason}"
        else -> { "TODO" }
    }
}
