package com.example.weatherapp.search

import com.example.data.api.GeoApi
import com.example.data.api.response.city.CityItem
import com.example.data.domain.mapToCitiesList
import com.example.data.repository.SearchRepository
import com.example.data.search.SearchState
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.SpyUiController
import com.example.weatherapp.TestCoroutineDispatchers
import com.example.weatherapp.validation.StringValidator
import com.example.weatherapp.viewmodel.CitySearchViewModel
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
    private lateinit var locationDeferred: Deferred<List<CityItem>>

    private lateinit var uiController: CitySearchUiController

    private val city1 = CityItem("Chicago", 41.8755616, -87.6244212, "Illinois", "US")
    private val city2 = CityItem("Chicago", -33.71745, 18.9963167, "Western Cape", "ZA")
    private val showLoading = SearchState.Loading(true)
    private val hideLoading = SearchState.Loading(false)
    private val cities = mutableListOf<CityItem>()

    @BeforeEach
    fun setUp() {
        val validator = StringValidator()
        val repository = SearchRepository(geoApi)
        val dispatchers = TestCoroutineDispatchers()
        val viewModel = CitySearchViewModel(dispatchers, repository, validator)

        uiController = CitySearchUiController().also { uiController ->
            uiController.viewModel = viewModel
        }
        // Mimic lifecycle start
        uiController.onCreate()
    }

    @Test
    fun `performs search for a given input`() = runBlocking {
        val userInput = "someCity"
        cities.addAll(listOf(city1, city2))
        val results = SearchState.CitiesResult(cities.mapToCitiesList())

        // Simulate API call
        coEvery { geoApi.searchCityAsync(userInput) }.returns(locationDeferred)
        coEvery { locationDeferred.await() }.answers { cities }

        // When the ui controller queries the repository
        uiController.search(userInput)

        // Verify that the UI state is as expected
        val expectedUiStates = listOf(showLoading, hideLoading, results)
        assertEquals(expectedUiStates, uiController.uiStates)
    }

    class CitySearchUiController : SpyUiController() {
        lateinit var viewModel: CitySearchViewModel
        val uiStates = mutableListOf<SearchState>()

        override fun onCreate() {
            super.onCreate()
            viewModel.searchLiveData.observe(this) { uiState ->
                uiStates.add(uiState)
            }
        }

        fun search(userInput: String) = runBlocking {
            viewModel.search(userInput)
        }
    }
}
