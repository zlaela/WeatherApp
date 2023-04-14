package com.example.data.repository

import com.example.data.api.TestHelpers
import com.example.data.api.WeatherApi
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
import com.example.data.domain.mapToForecast
import com.example.data.exception.HttpException
import com.example.data.search.WeatherResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WeatherSearchRepositoryShould {
    @RelaxedMockK
    private lateinit var weatherApi: WeatherApi

    @MockK
    private lateinit var currentWeatherDeferred: Deferred<CurrentWeatherResponse>

    @MockK
    private lateinit var forecastDeferred: Deferred<FiveDayForecastResponse>

    private val someCity: City = City("US", "IL","Chicago", 41.8755616, -87.6244212, 4896348)

    private lateinit var repository: WeatherSearchRepository

    @BeforeEach
    fun setUp() {
        repository = WeatherSearchRepository(weatherApi)
    }

    @Test
    fun `return current weather when search succeeds`() = runBlocking {
        val expectedResponse = getWeatherResponse()
        val expectedWeather = expectedResponse.mapToCurrentWeather()

        coEvery { weatherApi.getCurrentWeather(someCity.name) }.coAnswers { currentWeatherDeferred }
        coEvery { currentWeatherDeferred.await() }.coAnswers { expectedResponse }

        // When search is called with a city
        val weather = repository.getCurrentWeather(someCity)

        // The repository returns a current weather result
        assertEquals(weather, WeatherResult.WeatherSuccess(expectedWeather))
    }

    @Test
    fun `return forecast for a city when search succeeds`() = runBlocking {
        val expectedResponse = getForecastResponse()
        val expectedForecast = expectedResponse.mapToForecast()

        coEvery { weatherApi.getForecast(someCity.lat, someCity.lon) }.coAnswers { forecastDeferred }
        coEvery { forecastDeferred.await() }.coAnswers { expectedResponse }

        // When search is called with a city
        val forecast = repository.getForecast(someCity)

        // The repository returns he forecast
        assertEquals(forecast, WeatherResult.ForecastSuccess(expectedForecast))
    }

    @Test
    fun `return failure when API exception occurs`() = runBlocking {
        coEvery { weatherApi.getForecast(someCity.lat, someCity.lon) }.throws(HttpException())

        // when the search is performed for some city
        val result = repository.getForecast(someCity)

        // The repository returns a failure
        assertEquals(result, WeatherResult.Failure("Bad"))
    }

    private fun getWeatherResponse(): CurrentWeatherResponse =
        TestHelpers.getWeatherResponse(CurrentWeatherResponse::class.java, "weather.json")

    private fun getForecastResponse(): FiveDayForecastResponse =
        TestHelpers.getWeatherResponse(FiveDayForecastResponse::class.java, "forecast.json")
}