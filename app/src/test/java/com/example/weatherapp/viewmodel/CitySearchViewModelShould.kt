package com.example.weatherapp.viewmodel

import com.example.data.repository.SearchRepository
import com.example.data.search.SearchState
import com.example.weatherapp.ExecutionExtension
import com.example.weatherapp.validation.StringValidator
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ExecutionExtension::class, MockKExtension::class)
class CitySearchViewModelShould {
    @RelaxedMockK
    private lateinit var validator: StringValidator

    @RelaxedMockK
    private lateinit var repository: SearchRepository

    private lateinit var citySearchViewModel: CitySearchViewModel

    private val someCity = "some_city"

    @BeforeEach
    fun setUp() {
        citySearchViewModel = CitySearchViewModel(repository, validator)
    }

    @Test
    fun `validate the search string`() {
        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The validator validates the string
        verify(exactly = 1) {
            validator.validate(someCity)
        }
    }

    @Test
    fun `search the repository with a valid string`() {
        // Set up valid search string
        every { validator.validate(someCity) }.answers { true }

        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The repository performs a search with the given string
        verify(exactly = 1) {
            repository.search(someCity)
        }
    }

    @Test
    fun `does not search the repository with an invalid string`() {
        // Set up invalid search string
        every { validator.validate(someCity) }.answers { false }

        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The repository is not called
        verify { repository wasNot Called }
    }

    @Test
    fun `notify when the query is not valid`() {
        // Set up invalid search string
        every { validator.validate(someCity) }.answers { false }

        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The ui state is Invalid
        assertEquals(SearchState.InvalidString, citySearchViewModel.searchLiveData.value)
    }
}