package com.example.weatherapp.search

import androidx.lifecycle.Observer
import com.example.data.api.WeatherApi
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse
import com.example.data.domain.*
import com.example.data.repository.DataStoreRepository
import com.example.data.repository.WeatherSearchRepository
import com.example.data.search.WeatherResult
import com.example.data.store.DataStoreState
import com.example.data.store.PreferencesDataSource
import com.example.data.store.UserPreferences
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.SpyUiController
import com.example.weatherapp.TestCoroutineDispatchers
import com.example.weatherapp.TestHelpers
import com.example.weatherapp.viewmodel.WeatherSearchViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/** Weather search feature will
 * Handle successful weather search result
 * Handle unsuccessful weather search result
 * Look for a saved location in datastore and update weather if it exists
 */
@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class WeatherSearchTest {
    @MockK
    private lateinit var dataSource: PreferencesDataSource

    @MockK
    private lateinit var weatherApi: WeatherApi

    @MockK
    private lateinit var forecastDeferred: Deferred<FiveDayForecastResponse>

    @MockK
    private lateinit var weatherDeferred: Deferred<CurrentWeatherResponse>

    @MockK
    private lateinit var weatherLiveDataObserver: Observer<WeatherResult>

    private val someCity: City = City("", "", "Chicago", 41.8755616, -87.6244212)
    private val showLoading: WeatherResult = WeatherResult.Loading(true)
    private val hideLoading: WeatherResult = WeatherResult.Loading(false)
    private lateinit var weatherSearchViewModel: WeatherSearchViewModel

    private lateinit var uiController: WeatherSearchUiController

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        val weatherRepository = WeatherSearchRepository(weatherApi)
        val dataStoreRepository = DataStoreRepository(dataSource)

        // Mock the PreferencesDataSource methods that will be called
        coEvery { dataSource.getPreferences() } returns UserPreferences()
        coEvery { dataSource.updateCity(any()) } coAnswers { mockk() }
        coEvery { dataSource.updateCountry(any()) } coAnswers { mockk() }

        weatherSearchViewModel =
            WeatherSearchViewModel(dispatchers, weatherRepository, dataStoreRepository)
        uiController = WeatherSearchUiController().also {
            it.viewModel = weatherSearchViewModel
        }
    }

    @Test
    fun `looks for saved city in prefs on startup`() = runBlocking {

        clearMocks(dataSource)

        val emptyPrefs = UserPreferences()
        coEvery { dataSource.getPreferences() }.answers { emptyPrefs }

        // When the view is created
        uiController.onCreate()

        // The controller subscribes to prefs updates
        Assertions.assertTrue(weatherSearchViewModel.getPrefsOnStart.hasActiveObservers())
        coVerify(exactly = 1) { dataSource.getPreferences() }

        // The response state is as expected for empty prefs
        val successState = DataStoreState.GetPreferencesSuccess(null, null)
        assertEquals(successState, uiController.dataStoreStates.first())
    }

    @Test
    fun `updates weather for last location if found on startup`() = runBlocking {
        clearMocks(dataSource)

        val cityPrefs = UserPreferences(null, someCity.name, someCity.lat, someCity.lon)
        coEvery { dataSource.getPreferences() } returns cityPrefs
        coEvery { dataSource.updateCity(any()) } coAnswers { mockk() }
        coEvery { dataSource.updateCountry(any()) } coAnswers { mockk() }

        val expectedWeather = setUpWeatherResults()

        // Mimic datastore returns prefs
        coEvery { dataSource.getPreferences() }.coAnswers { cityPrefs }

        // When the view is created
        uiController.onCreate()

        // The datastore returns the expected preferences
        coVerify(exactly = 1) { dataSource.getPreferences() }
        val successState = DataStoreState.GetPreferencesSuccess(someCity, cityPrefs.country)
        assertEquals(successState, uiController.dataStoreStates.first())

        // And the viewmodel updates the weather for the city
        verifySequence {
            weatherLiveDataObserver.onChanged(showLoading)
            weatherLiveDataObserver.onChanged(hideLoading)
            weatherLiveDataObserver.onChanged(WeatherResult.WeatherSuccess(expectedWeather))
        }
    }

    @Test
    fun `performs forecast lookup for a given city`() = runBlocking {

        // When the viewModel searches for the forecast
        weatherSearchViewModel.getForecast(someCity)

        // Verify that livedata emits loading state, weather result, and hide loading states in order
        verifySequence {
            weatherLiveDataObserver.onChanged(showLoading)
            weatherLiveDataObserver.onChanged(hideLoading)
        }
    }

    @Test
    fun `performs weather lookup for a given city`() = runBlocking {
        val expectedWeather = setUpWeatherResults()

        // When the viewModel searches for the weather
        weatherSearchViewModel.getWeatherFor(someCity)

        // Verify that livedata emits loading state, weather result, and hide loading states in order
        verifySequence {
            weatherLiveDataObserver.onChanged(showLoading)
            weatherLiveDataObserver.onChanged(hideLoading)
            weatherLiveDataObserver.onChanged(WeatherResult.WeatherSuccess(expectedWeather))
        }
    }


    private fun setUpWeatherResults(): CurrentWeather {
        val expectedResponse = getWeatherResponse()

        // Ensure every change is emitted
        every { weatherLiveDataObserver.onChanged(any()) }.answers { }
        coEvery { weatherApi.getCurrentWeather(someCity.name) }.coAnswers { weatherDeferred }
        coEvery { weatherDeferred.await() }.coAnswers { expectedResponse }
        // Viewmodel updates subscribed
        weatherSearchViewModel.weatherLiveData.observeForever(weatherLiveDataObserver)

        return expectedResponse.mapToCurrentWeather()
    }

    // TODO: This is duplicated and should  be moved to a common testing package
    private fun getWeatherResponse(): CurrentWeatherResponse =
        TestHelpers.getWeatherResponse(CurrentWeatherResponse::class.java, "weather.json")

    // TODO: This is duplicated and should  be moved to a common testing package
    private fun getForecastResponse(): FiveDayForecastResponse =
        TestHelpers.getWeatherResponse(FiveDayForecastResponse::class.java, "forecast.json")

    class WeatherSearchUiController : SpyUiController() {
        lateinit var viewModel: WeatherSearchViewModel
        val dataStoreStates = mutableListOf<DataStoreState>()

        override fun onCreate() {
            super.onCreate()
            viewModel.getPrefsOnStart.observe(this) { dataStoreState ->
                dataStoreStates.add(dataStoreState)
                when (dataStoreState) {
                    is DataStoreState.GetPreferencesSuccess -> {
                        dataStoreState.city
                            ?.let { nnCity ->
                                println("Got city: $nnCity")
                                viewModel.getWeatherFor(nnCity)
                            }
                    }
                    else -> {
                        println("No success datastore")
                        /** TODO: Nothing for now. Maybe device location search? */
                    }
                }
            }
        }
    }
}