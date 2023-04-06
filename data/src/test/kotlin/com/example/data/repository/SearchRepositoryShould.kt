package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.search.SearchState
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SearchRepositoryShould {
    @RelaxedMockK
    private lateinit var geoApi: GeoApi

    private lateinit var repository: SearchRepository

    @BeforeEach
    fun setUp() {
        repository = SearchRepository(geoApi)
    }

    @Test
    fun `return location when search succeeds`() {
        val someCity = "someCity"
        val matches = listOf("City 1", "City 2")

        // Geo Api search for a city
        every { geoApi.searchCity(someCity) }.answers { matches }

        // When the search is called with a term
        val expectedResult = repository.search(someCity)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.Results(matches))
    }
}