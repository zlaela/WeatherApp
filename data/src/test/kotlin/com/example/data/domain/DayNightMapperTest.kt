package com.example.data.domain

import com.example.data.api.TestHelpers
import com.example.data.api.response.weather.FiveDayForecastResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.filter
import kotlin.collections.filterKeys

class DayNightMapperTest {
    private val numForecastDays = 6
    private val numForecastItems = 40
    private val numDayForecasts = 18 // because today's day/night is removed from the forecast
    private val numNightForecasts = 18 // because today's day/night is removed from the forecast

    companion object {
        private lateinit var sut:List<DayNightForecast>
        private lateinit var forecasts: List<Forecast>
        private lateinit var todayFromData: LocalDate
        private lateinit var forecastsByDate: Map<LocalDate, List<Forecast>>

        @JvmStatic
        @BeforeAll
        fun setUp() {
            val forecastResponse = TestHelpers.getWeatherResponse(
                FiveDayForecastResponse::class.java,
                "big_forecast.json"
            )
            forecasts = forecastResponse.mapToForecast()

            // Get the "today" date from the first forecast item in the test data
            val firstForecast = forecasts.first()
            todayFromData = Instant.ofEpochSecond(firstForecast.forecastDateTimeUtc.toLong())
                .atZone(ZoneId.systemDefault()).toLocalDate()

            forecastsByDate = forecasts.groupBy { forecast ->
                val instant = Instant.ofEpochSecond(forecast.forecastDateTimeUtc.toLong())
                instant.atZone(ZoneId.systemDefault()).toLocalDate()
            }
            sut = forecasts.mapToDayNightForecast()
        }
    }

    // Validate logic of mapper
    @Test
    fun `mapper should group forecasts by day`() {
        // Verify 40 forecasts
        assertEquals(numForecastItems, forecasts.size, "Should have $numForecastItems forecast")

        // Verify 6 forecast days
        assertEquals(numForecastDays, forecastsByDate.size, "Expected $numForecastDays forecast days")

        val dateForecasts: Map<LocalDate, Pair<List<Forecast>, List<Forecast>>> = forecastsByDate.entries.associate { (date, forecasts) ->
            val nights: List<Forecast> = forecasts.filter { it.partOfDay == "n" }
            val days: List<Forecast> = forecasts.filter { it.partOfDay == "d" }
            date to (days to nights)
        }

        // Verify that today is excluded from future dates
        val futureDates = dateForecasts.filterKeys { it.isAfter(todayFromData) }
        assertFalse(futureDates.contains(todayFromData), "Today should be excluded from future dates")

        // Verify day and night forecast counts
        val (numDays, numNights) = futureDates.values.fold(0 to 0) { acc, pair ->
            val days = pair.first.size
            val nights = pair.second.size
            (acc.first + days) to (acc.second + nights)
        }

        assertEquals(numDayForecasts, numDays, "Should have $numDayForecasts day forecasts in input data")
        assertEquals(numNightForecasts, numNights, "Should have $numNightForecasts day forecasts in input data")

        // Verify temperatures are in order
        futureDates.forEach { (date, dayNight) ->
            val dayForecasts = dayNight.first
            val nightForecasts = dayNight.second
            if (dayForecasts.isNotEmpty()) {
                val dayHigh = dayForecasts.maxOfOrNull { it.tempMax } ?: 0.0
                val dayLow = dayForecasts.minOfOrNull { it.tempMin } ?: 0.0
                assertTrue(dayHigh >= dayLow, "Day high should be >= day low for date $date")
            }

            if (nightForecasts.isNotEmpty()) {
                val nightHigh = nightForecasts.maxOfOrNull { it.tempMax } ?: 0.0
                val nightLow = nightForecasts.minOfOrNull { it.tempMin } ?: 0.0
                assertTrue(
                    nightHigh >= nightLow,
                    "Night high should be >= night low for date $date"
                )
            }
        }

        // Verify that paired forecast dates are sorted chronologically
        val firstOutOfOrder = futureDates.keys.toList().zipWithNext().firstOrNull { (a, b) -> b < a }
        assertTrue(
            firstOutOfOrder == null,
            "Forecasts should be sorted chronologically || ${firstOutOfOrder?.second} vs ${firstOutOfOrder?.first}"
        )
    }

    @Test
    fun `mapper should sort day and night temperatures correctly`() {
        val datesInInput = forecasts.map { forecast ->
            Instant.ofEpochSecond(forecast.forecastDateTimeUtc.toLong())
                .atZone(ZoneId.systemDefault()).toLocalDate()
        }.toSet()

        datesInInput.forEach { date ->
            val forecastsForDate = forecasts.filter { forecast ->
                val forecastDate =
                    Instant.ofEpochSecond(forecast.forecastDateTimeUtc.toLong())
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                forecastDate == date
            }

            val dayForecasts = forecastsForDate.filter { it.partOfDay == "d" }
            val nightForecasts = forecastsForDate.filter { it.partOfDay == "n" }

            if (dayForecasts.isNotEmpty()) {
                val dayHigh = dayForecasts.maxOfOrNull { it.tempMax } ?: 0.0
                val dayLow = dayForecasts.minOfOrNull { it.tempMin } ?: 0.0
                assertTrue(dayHigh >= dayLow, "Day high should be >= day low for date $date")
            }

            if (nightForecasts.isNotEmpty()) {
                val nightHigh = nightForecasts.maxOfOrNull { it.tempMax } ?: 0.0
                val nightLow = nightForecasts.minOfOrNull { it.tempMin } ?: 0.0
                assertTrue(
                    nightHigh >= nightLow,
                    "Night high should be >= night low for date $date"
                )
            }
        }
    }

    // Sanity check for the mapper class
    @Test
    fun `mapper correctly creates DayNightForecast object`() {
        val testDayNightForecast = forecastsByDate.entries.associate { (date, forecasts) ->
            val nights: List<Forecast> = forecasts.filter { it.partOfDay == "n" }
            val days: List<Forecast> = forecasts.filter { it.partOfDay == "d" }
            date to (days to nights)
        }
            .filterKeys { it.isAfter(todayFromData) }
            .map { (date, forecastMap) ->
                val dayForecasts = forecastMap.first
                val nightForecasts = forecastMap.second

                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, EEEE", Locale.getDefault())
                val formattedDate = date.format(dateFormatter)

                DayNightForecast(
                    dateString = formattedDate,
                    localDate = date,
                    dayForecast = dayForecasts.maxByOrNull { it.tempMax }, // Warmest day
                    nightForecast = nightForecasts.minByOrNull { it.tempMin }, // Coolest night
                    dayHigh = dayForecasts.maxOfOrNull { it.tempMax } ?: 0.0, // Highest day temp
                    dayLow = dayForecasts.minOfOrNull { it.tempMin } ?: 0.0,  // Lowest day temp
                    nightHigh = nightForecasts.maxOfOrNull { it.tempMax }
                        ?: 0.0, // Highest night temp
                    nightLow = nightForecasts.minOfOrNull { it.tempMin }
                        ?: 0.0   // Lowest night temp
                )
            }

        // Assert that the sut DayNightForecast and the testDayNightForecast are the same
        assertEquals(sut.size, testDayNightForecast.size, "Forecasts should have the same size")

        sut.zip(testDayNightForecast).forEachIndexed { index, (sutItem, testItem) ->
            assertEquals(sutItem.dateString, testItem.dateString, "DateString mismatch at index $index")
            assertEquals(sutItem.localDate, testItem.localDate, "LocalDate mismatch at index $index")
            assertEquals(sutItem.dayForecast, testItem.dayForecast, "DayForecast mismatch at index $index")
            assertEquals(sutItem.nightForecast, testItem.nightForecast, "NightForecast mismatch at index $index")
            assertEquals(sutItem.dayHigh, testItem.dayHigh, "DayHigh mismatch at index $index")
            assertEquals(sutItem.dayLow, testItem.dayLow, "DayLow mismatch at index $index")
            assertEquals(sutItem.nightHigh, testItem.nightHigh, "NightHigh mismatch at index $index")
            assertEquals(sutItem.nightLow, testItem.nightLow, "NightLow mismatch at index $index")
        }
    }
}