package com.example.weatherapp.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.viewmodel.CitySearchViewModel
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
@ExtendWith(ExecutionExtension::class)
class SearchCityTest {
    private lateinit var uiController: SpyUiController

    private val showLoading = SearchState.ShowLoading
    private val hideLoading = SearchState.HideLoading

    @BeforeEach
    fun setUp() {
        val viewModel = CitySearchViewModel()
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

        // When the ui controller queries the repository
        uiController.search(userInput)

        // Verify that the UI state is as expected
        val results = SearchState.Results(cities)
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
