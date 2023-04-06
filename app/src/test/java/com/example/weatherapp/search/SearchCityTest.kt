package com.example.weatherapp.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.data.api.GeoApi
import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.City
import com.example.data.repository.SearchRepository
import com.example.data.search.SearchState
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.TestCoroutineDispatchers
import com.example.weatherapp.validation.StringValidator
import com.example.weatherapp.viewmodel.CitySearchViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * City search feature will:
 * 1. Handle successful city search result
 * 2. Handle failed city search result
 * 3. Not attempt to search if the input is invalid
 */
@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class SearchCityTest {
    @RelaxedMockK
    private lateinit var geoApi: GeoApi

    @MockK
    private lateinit var locationDeferred: Deferred<Cities>

    private lateinit var uiController: SpyUiController

    private val showLoading = SearchState.ShowLoading
    private val hideLoading = SearchState.HideLoading

    private val cities: Cities = Cities()
    private val city1 = City("Chicago", 41.8755616, -87.6244212, "Illinois", "US")
    private val city2 = City("Chicago", -33.71745, 18.9963167, "Western Cape", "ZA")

    @BeforeEach
    fun setUp() {
        val validator = StringValidator()
        val repository = SearchRepository(geoApi)
        val dispatchers = TestCoroutineDispatchers()
        val viewModel = CitySearchViewModel(dispatchers, repository, validator)

        uiController = SpyUiController().also { uiController ->
            uiController.viewModel = viewModel
        }
        // Mimic lifecycle start
        uiController.onCreate()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performs search for a given input`() = runTest {
        val userInput = "someCity"
        cities.addAll(listOf(city1, city2))
        val results = SearchState.CitiesResult(cities)

        // Simulate API call
        coEvery { geoApi.searchCityAsync(userInput) }.returns(locationDeferred)
        coEvery { locationDeferred.await() }.answers { cities }

        // When the ui controller queries the repository
        uiController.search(userInput)
        advanceUntilIdle()

        // Verify that the UI state is as expected
        val expectedUiStates = listOf(showLoading, results, hideLoading)
        assertEquals(expectedUiStates, uiController.uiStates)
    }

    class SpyUiController : LifecycleOwner {
        private lateinit var lifecycleRegistry: LifecycleRegistry
        lateinit var viewModel: CitySearchViewModel

        val uiStates = mutableListOf<SearchState>()

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry

        fun search(userInput: String) = runBlocking {
            viewModel.search(userInput)
        }

        fun onCreate() {
            lifecycleRegistry = LifecycleRegistry(this)
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            viewModel.searchLiveData.observe(this) { uiState ->
                uiStates.add(uiState)
            }
        }
    }
}
