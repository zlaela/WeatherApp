package com.example.weatherapp.search

import androidx.lifecycle.Observer
import com.example.data.api.WeatherApi
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.domain.City
import com.example.data.domain.mapToCurrentWeather
import com.example.data.repository.WeatherSearchRepository
import com.example.data.search.WeatherResult
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.TestCoroutineDispatchers
import com.example.weatherapp.TestHelpers
import com.example.weatherapp.viewmodel.WeatherSearchViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/** Weather search feature will
 * Handle successful weather search result
 * Handle unsuccessful weather search result
 */
@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class WeatherSearchTest {
    @MockK
    private lateinit var weatherApi: WeatherApi

    @MockK
    private lateinit var weatherDeferred: Deferred<CurrentWeatherResponse>

    @MockK
    private lateinit var weatherLiveDataObserver: Observer<WeatherResult>

    private val someCity: City = City("Chicago", 41.8755616, -87.6244212)
    private val showLoading: WeatherResult = WeatherResult.ShowLoading
    private val hideLoading: WeatherResult = WeatherResult.HideLoading
    private lateinit var weatherSearchViewModel: WeatherSearchViewModel

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        val weatherRepository = WeatherSearchRepository(weatherApi)
        weatherSearchViewModel = WeatherSearchViewModel(dispatchers, weatherRepository)
    }

    @Test
    fun `performs weather lookup for given city`() = runBlocking {
        val expectedResponse = getWeatherResponse()
        val expectedWeather = expectedResponse.mapToCurrentWeather()

        // Ensure every change is emitted
        every { weatherLiveDataObserver.onChanged(any()) }.answers { }
        coEvery { weatherApi.getCurrentWeather(someCity) }.returns(weatherDeferred)
        coEvery { weatherDeferred.await() }.coAnswers { expectedResponse }

        weatherSearchViewModel.weatherLiveData.observeForever(weatherLiveDataObserver)

        // When the viewModel searches for a city
        weatherSearchViewModel.getWeatherFor(someCity)

        // Verify that livedata emits loading state, weather result, and hide loading states in order
        verifySequence {
            weatherLiveDataObserver.onChanged(showLoading)
            weatherLiveDataObserver.onChanged(WeatherResult.Success(expectedWeather))
            weatherLiveDataObserver.onChanged(hideLoading)
        }
    }

    // TODO: This is duplicated and should  be moved to a common testing package
    private fun getWeatherResponse(): CurrentWeatherResponse =
        TestHelpers.getWeatherResponse(CurrentWeatherResponse::class.java, "weather.json")
}