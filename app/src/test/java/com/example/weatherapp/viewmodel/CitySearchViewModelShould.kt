package com.example.weatherapp.viewmodel

import com.example.weatherapp.repository.SearchRepository
import com.example.weatherapp.validation.StringValidator
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CitySearchViewModelShould {
    @RelaxedMockK
    private lateinit var validator: StringValidator

    @RelaxedMockK
    private lateinit var repository: SearchRepository

    private lateinit var citySearchViewModel: CitySearchViewModel

    @BeforeEach
    fun setUp() {
        citySearchViewModel = CitySearchViewModel(repository, validator)
    }

    @Test
    fun `validate the search string`() {
        val someCity = "some_city"

        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The validator validates the string
        verify(exactly = 1) {
            validator.validate(someCity)
        }
    }

    @Test
    fun `search the repository with a valid string`() {
        val someCity = "some_city"
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
        val someCity = "some_city"
        // Set up invalid search string
        every { validator.validate(someCity) }.answers { false }

        // When search is called in the view model
        citySearchViewModel.search(someCity)

        // The repository is not called
        verify { repository wasNot Called }
    }
}