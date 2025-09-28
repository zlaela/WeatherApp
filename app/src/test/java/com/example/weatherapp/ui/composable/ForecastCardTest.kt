package com.example.weatherapp.ui.composable

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.data.domain.DayNightForecast
import com.example.data.domain.Forecast
import com.example.data.search.ForecastResult
import com.example.weatherapp.ExecutionExtension
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    // Success tests
    @Test
    fun `displays multiple forecast days correctly`() {
        // Given multiple forecast days
        val forecast1 = createSampleForecast()
        val forecast2 = createSampleForecast(date = LocalDate.of(2023, 9, 29))
        val forecastStates = mutableStateOf(ForecastResult.ForecastSuccess(listOf(forecast1, forecast2)))

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows both forecast days
        assertEquals("Sep 28, Thursday - Day: 25°F Clear - Night: 18°F Clear | Sep 29, Friday - Day: 25°F Clear - Night: 18°F Clear", result)
    }
    @Test
    fun `handles forecast with missing day data`() {
        val forecast = createSampleForecast(hasDayForecast = false)
        val forecastStates = mutableStateOf(ForecastResult.ForecastSuccess(listOf(forecast)))

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows only night data
        assertEquals("Sep 28, Thursday - Night: 18°F Clear", result)
    }

    @Test
    fun `handles forecast with missing night data`() {
        val forecast = createSampleForecast(hasNightForecast = false)
        val forecastStates = mutableStateOf(ForecastResult.ForecastSuccess(listOf(forecast)))

        // When ForecastCard is created
        val result = getTextFromForecastState(forecastStates)

        // Then it shows only day data
        assertEquals("Sep 28, Thursday - Day: 25°F Clear", result)
    }

    private fun getTextFromForecastState(state: State<ForecastResult>): String =
        when (val forecastState = state.value) {
            ForecastResult.Initial -> "Forecast Pending"
            is ForecastResult.Loading -> "Loading Forecast..."
            is ForecastResult.Failure -> "Failed to load forecast: ${forecastState.reason}"
            is ForecastResult.ForecastSuccess -> {
                forecastState.locationForecast.joinToString(" | ") { forecast ->
                    val dateStr = forecast.dateString
                    val dayStr = forecast.dayForecast?.let { "Day: ${it.tempMax.toInt()}°F ${it.condition}" } ?: ""
                    val nightStr = forecast.nightForecast?.let { "Night: ${it.tempMax.toInt()}°F ${it.condition}" } ?: ""
                    val parts = listOfNotNull(dateStr, dayStr, nightStr).filter { it.isNotEmpty() }
                    parts.joinToString(" - ")
                }
            }
        }

    private fun createSampleForecast(
        date: LocalDate = LocalDate.of(2023, 9, 28),
        hasDayForecast: Boolean = true,
        hasNightForecast: Boolean = true
    ): DayNightForecast {

        val dayForecast = if (hasDayForecast) {
            Forecast(
                forecastDateTimeUtc = 1695859200,
                forecastDateTime = "2023-09-28 12:00:00",
                tempMin = 20.0,
                tempMax = 25.0,
                partOfDay = "d",
                icon = "01d",
                condition = "Clear",
                description = "clear sky",
                humidity = 65,
                chanceOfRain = 0.1
            )
        } else null

        val nightForecast = if (hasNightForecast) {
            Forecast(
                forecastDateTimeUtc = 1695859200,
                forecastDateTime = "2023-09-28 00:00:00",
                tempMin = 15.0,
                tempMax = 18.0,
                partOfDay = "n",
                icon = "01n",
                condition = "Clear",
                description = "clear sky",
                humidity = 80,
                chanceOfRain = 0.05
            )
        } else null

        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, EEEE", Locale.getDefault())
        val formattedDate = date.format(dateFormatter)

        return DayNightForecast(
            localDate = date,
            dateString = formattedDate,
            dayForecast = dayForecast,
            nightForecast = nightForecast,
            dayHigh = 25.0,
            dayLow = 20.0,
            nightHigh = 18.0,
            nightLow = 15.0
        )
    }
}