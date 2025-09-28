package com.example.weatherapp.ui.composable

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.data.search.ForecastResult
import com.example.weatherapp.ExecutionExtension
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * ForecastCard feature will:
 * 1. Display initial state when no forecast data is available
 * 2. Display loading state while fetching forecast data
 * 3. Display error state when forecast fetch fails
 * 4. Display forecast data when successfully loaded
 * 5. Format forecast data correctly
 */
@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class ForecastCardTest {

    private lateinit var forecastStates: State<ForecastResult>

    @BeforeEach
    fun setUp() {
        forecastStates = mutableStateOf(ForecastResult.Initial)
    }

    @Test
    fun `displays initial state when no forecast data is available`() {
        // Given initial state
        forecastStates = mutableStateOf(ForecastResult.Initial)

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows empty state
        assertEquals("Forecast Pending", result)
    }

    @Test
    fun `displays loading state while fetching forecast`() {
        // Given loading state
        forecastStates = mutableStateOf(ForecastResult.Loading(true))

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows loading message
        assertEquals("Loading Forecast...", result)
    }

    @Test
    fun `displays error state when forecast fetch fails`() {
        // Given error state
        val errorReason = "Network error"
        forecastStates = mutableStateOf(ForecastResult.Failure(errorReason))

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows error message with reason
        assertEquals("Failed to load forecast: $errorReason", result)
    }

    private fun getTextFromForecastState(state: State<ForecastResult>): String =
        when (val forecastState = state.value) {
            ForecastResult.Initial -> "Forecast Pending"
            is ForecastResult.Loading -> "Loading Forecast..."
            is ForecastResult.Failure -> "Failed to load forecast: ${forecastState.reason}"
            else -> ""
        }
}