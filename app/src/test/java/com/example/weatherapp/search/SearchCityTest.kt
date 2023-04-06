package com.example.weatherapp.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.data.api.GeoApi
import com.example.data.repository.SearchRepository
import com.example.data.search.SearchState
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.validation.StringValidator
import com.example.weatherapp.viewmodel.CitySearchViewModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
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
    private lateinit var uiController: SpyUiController

    private val showLoading = SearchState.ShowLoading
    private val hideLoading = SearchState.HideLoading

    @BeforeEach
    fun setUp() {
        val validator = StringValidator()
        val repository = SearchRepository(geoApi)
        val viewModel = CitySearchViewModel(repository, validator)
        uiController = SpyUiController().also { uiController ->
            uiController.viewModel = viewModel
        }
        // Mimic lifecycle start
        uiController.onCreate()
    }

    @Test
    fun `performs search for a given input`() {
        val userInput = "someCity"
        val cities = listOf("someCityMatch")
        val results = SearchState.Results(cities)

        // Simulate API call
        every { geoApi.searchCity(userInput) }.answers { cities }

        // When the ui controller queries the repository
        uiController.search(userInput)

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

        fun search(userInput: String) {
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
