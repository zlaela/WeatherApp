package com.example.weatherapp.viewmodel

import com.example.weatherapp.validation.StringValidator
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

    private lateinit var citySearchViewModel: CitySearchViewModel

    @BeforeEach
    fun setUp() {
        citySearchViewModel = CitySearchViewModel(validator)
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
}