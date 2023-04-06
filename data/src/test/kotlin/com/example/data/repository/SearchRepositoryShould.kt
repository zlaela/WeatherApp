package com.example.data.repository

import com.example.data.api.GeoApi
import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.CityItem
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
    private lateinit var citiesDeferred: Deferred<Cities>
    @MockK
    private lateinit var cityDeferred: Deferred<CityItem>

    private lateinit var repository: SearchRepository

    private val cities: Cities = Cities()
    private val city1 = CityItem("Chicago", 41.8755616, -87.6244212, "Illinois", "US")
    private val city2 = CityItem("Chicago", -33.71745, 18.9963167, "Western Cape", "ZA")

    @BeforeEach
    fun setUp() {
        repository = SearchRepository(geoApi)
    }

    @Test
    fun `return locations when search succeeds`() = runBlocking {
        val someCity = "someCity"
        cities.addAll(listOf(city1, city2))

        // Geo Api search for a city
        coEvery { geoApi.searchCityAsync(someCity) }.coAnswers { citiesDeferred }
        coEvery { citiesDeferred.await() }.coAnswers { cities }

        // When the search is called with a term
        val expectedResult = repository.searchCity(someCity)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.CitiesResult(cities))
    }

    @Test
    fun `return location when search by zip succeeds`() = runBlocking {
        val someZip = "60652"

        // Geo Api search for a city
        coEvery { geoApi.searchZipAsync(someZip) }.coAnswers { cityDeferred }
        coEvery { cityDeferred.await() }.coAnswers { city1 }

        // When the search is called with a term
        val expectedResult = repository.searchZip(someZip)

        // The repository returns matching cities
        assertEquals(expectedResult, SearchState.ZipResult(city1))
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