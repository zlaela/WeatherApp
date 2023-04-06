package com.example.weatherapp.search

import androidx.lifecycle.Observer
import com.example.data.domain.City
import com.example.data.domain.CurrentWeather
import com.example.data.repository.WeatherSearchRepository
import com.example.data.search.WeatherResult
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.TestCoroutineDispatchers
import com.example.weatherapp.viewmodel.WeatherSearchViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyOrder
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
    private lateinit var weatherLiveDataObserver: Observer<WeatherResult>

    private val someCity: City = City("Chicago", 41.8755616, -87.6244212)
    private val locationWeather = CurrentWeather(
        "Chicago", 41.8755616, -87.6244212, 12.5, 40.3, 19.0, 20.0, "someIcon", "It's cold"
    )
    private val weatherResult = WeatherResult.Success(locationWeather)
    private val showLoading: WeatherResult = WeatherResult.ShowLoading
    private val hideLoading: WeatherResult = WeatherResult.HideLoading
    private lateinit var weatherSearchViewModel: WeatherSearchViewModel

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        val weatherRepository = WeatherSearchRepository()
        weatherSearchViewModel = WeatherSearchViewModel(dispatchers, weatherRepository)
    }

    @Test
    fun `performs weather lookup for given city`() {
        // Ensure every change is emitted
        every { weatherLiveDataObserver.onChanged(any()) }.answers { }
        weatherSearchViewModel.weatherLiveData.observeForever(weatherLiveDataObserver)

        // When the viewModel searches for a city
        weatherSearchViewModel.getWeatherFor(someCity)

        // Verify that livedata emits loading state, weather result, and hide loading states in order
        verifyOrder {
            weatherLiveDataObserver.onChanged(showLoading)
            weatherLiveDataObserver.onChanged(weatherResult)
            weatherLiveDataObserver.onChanged(hideLoading)
        }
    }

}