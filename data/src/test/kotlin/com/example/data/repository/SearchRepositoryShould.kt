package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.search.SearchState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SearchRepositoryShould {
    @RelaxedMockK
    private lateinit var geoApi: GeoApi

    @MockK
    private lateinit var locationDeferred: Deferred<List<String>>

    private lateinit var repository: SearchRepository

    @BeforeEach
    fun setUp() {
        repository = SearchRepository(geoApi)
    }

    @Test
    fun `return location when search succeeds`() = runBlocking {
        val someCity = "someCity"
        val matches = listOf("City 1", "City 2")

        // Geo Api search for a city
        coEvery { geoApi.searchCityAsync(someCity) }.coAnswers { locationDeferred }
        coEvery { locationDeferred.await() }.coAnswers { matches }

        // When the search is called with a term
        val expectedResult = repository.search(someCity)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.Results(matches))
    }
}