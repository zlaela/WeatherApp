package com.example.data.repository

import com.example.data.api.TestHelpers
import com.example.data.api.WeatherApi
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
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

    private val someCity: City = City("Chicago", 41.8755616, -87.6244212)

    private lateinit var repository: WeatherSearchRepository

    @BeforeEach
    fun setUp() {
        repository = WeatherSearchRepository(weatherApi)
    }

    @Test
    fun `return current weather when search succeeds`() = runBlocking {
        val expectedResponse = getWeatherResponse()
        val expectedWeather = expectedResponse.mapToCurrentWeather()

        coEvery { weatherApi.getCurrentWeather(someCity) }.coAnswers { currentWeatherDeferred }
        coEvery { currentWeatherDeferred.await() }.coAnswers { expectedResponse }

        // When search is called with a city
        val weather = repository.getCurrentWeather(someCity)

        // The repository returns a current weather result
        assertEquals(weather, WeatherResult.Success(expectedWeather))
    }

    private fun getWeatherResponse(): CurrentWeatherResponse =
        TestHelpers.getWeatherResponse(CurrentWeatherResponse::class.java, "weather.json")
}