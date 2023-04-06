package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.exception.HttpException
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
    fun `return locations when search succeeds`() = runBlocking {
        val someCity = "someCity"
        val matches = listOf("City 1", "City 2")

        // Geo Api search for a city
        coEvery { geoApi.searchCityAsync(someCity) }.coAnswers { locationDeferred }
        coEvery { locationDeferred.await() }.coAnswers { matches }

        // When the search is called with a term
        val expectedResult = repository.searchCity(someCity)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.Results(matches))
    }

    @Test
    fun `return location when search by zip succeeds`() = runBlocking {
        val someZip = "60652"
        val match = listOf("Chicago")

        // Geo Api search for a city
        coEvery { geoApi.searchZipAsync(someZip) }.coAnswers { locationDeferred }
        coEvery { locationDeferred.await() }.coAnswers { match }

        // When the search is called with a term
        val expectedResult = repository.searchZip(someZip)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.Results(match))
    }

    @Test
    fun `return failure when API exception occurs`() = runBlocking {
        coEvery { geoApi.searchZipAsync("") }.throws(HttpException())

        // When the search is called with a term
        val expectedResult = repository.searchZip("")

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.Failure("Failed"))
    }
}