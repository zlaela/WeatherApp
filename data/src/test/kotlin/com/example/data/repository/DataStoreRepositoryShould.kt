package com.example.data.repository

import com.example.data.domain.City
import com.example.data.exception.DataStoreUpdateException
import com.example.data.store.PreferencesDataSource
import com.example.data.store.DataStoreState
import com.example.data.store.UserPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DataStoreRepositoryShould {
    @RelaxedMockK
    private lateinit var dataStore: PreferencesDataSource

    private lateinit var repository: DataStoreRepository

    private val country = "US"
    private val city = City("US", "IL","Chicago", 41.8755616, -87.6244212, 4896348)

    @BeforeEach
    fun setUp() {
        repository = DataStoreRepository(dataStore)
    }

    @Test
    fun `get preferences from datastore`() = runBlocking {
        val expectedPrefs = DataStoreState.GetPreferencesSuccess(null, null)

        // mimic DataStore call
        coEvery { dataStore.getPreferences() }.coAnswers { UserPreferences() }

        // When get preferences is called in the repository
        val repoResults = repository.getPreferences()

        // The repository returns the expected states
        assertEquals(expectedPrefs, repoResults)
    }

    @Test
    fun `updates city preferences`() = runBlocking {
        // When the repository sets the city
        repository.updateCity(city)

        // The repository updates the data store
        coVerify { dataStore.updateCity(city) }
    }

    @Test
    fun `updates country preferences`() = runBlocking {
        // When the repository sets the city
        repository.updateCountry(country)

        // The repository updates the data store
        coVerify { dataStore.updateCountry(country) }
    }

    @Test
    fun `notifies when prefs fetch fails`() = runBlocking {
        // mimic DataStore call
        coEvery { dataStore.getPreferences() }.throws(DataStoreUpdateException())

        // Failure state is produced
        val repoResult = repository.getPreferences()
        val expectedFailure = DataStoreState.FailedToGetPreferences

        assertEquals(expectedFailure, repoResult)
    }

    @Test
    fun `notifies when city update fails`() = runBlocking {
        // mimic DataStore call
        coEvery { dataStore.updateCity(any()) }.throws(DataStoreUpdateException())

        // Failure state is produced when update city fails
        val cityUpdateResult = repository.updateCity(city)
        val cityUpdateFailure = DataStoreState.FailedToSetPreferences("Chicago")

        assertEquals(cityUpdateFailure, cityUpdateResult)
    }

    @Test
    fun `notifies when country update fails`() = runBlocking {
        // mimic Datastore call
        coEvery { dataStore.updateCountry(country) }.throws(DataStoreUpdateException())

        // Failure state is produced when update city fails
        val countryUpdateResult = repository.updateCountry(country)
        val countryUpdateFailure = DataStoreState.FailedToSetPreferences("US")
        assertEquals(countryUpdateFailure, countryUpdateResult)
    }
}