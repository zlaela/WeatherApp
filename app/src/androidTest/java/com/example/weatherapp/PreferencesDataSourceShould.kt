package com.example.weatherapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.example.data.domain.City
import com.example.data.store.PreferencesDataSource
import com.example.data.store.UserPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ExecutionExtension::class)
class PreferencesDataSourceShould {
    private val testScope = TestScope()
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { testContext.preferencesDataStoreFile("location_prefs_test.pb") }
        )

    private val preferencesDataSource: PreferencesDataSource = PreferencesDataSource(testDataStore)

    @Test
    fun loadEmptyPrefs() {
        val expectedPrefs = UserPreferences()

        // The repository returns empty preferences when nothing has been set
        testScope.runTest {
            val loadedPrefs = preferencesDataSource.getPreferences()
            assertEquals(expectedPrefs, loadedPrefs)
            testDataStore.edit { it.clear() }
        }
    }

    @Test
    fun updateCity() {
        val city = City("Chicago", 41.8755616, -87.6244212, 4896348)
        val prefs = UserPreferences(null,"Chicago", 41.8755616, -87.6244212, 4896348)

        testScope.runTest {
            preferencesDataSource.updateCity(city)
            val prefsCity: UserPreferences = preferencesDataSource.preferencesFlow.first()
            assertEquals(prefs, prefsCity)
            testDataStore.edit { it.clear() }
        }
    }

    @Test
    fun updateCountry() {
        val country = "US"

        testScope.runTest {
            preferencesDataSource.updateCountry(country)
            val prefsCountry = preferencesDataSource.preferencesFlow.first().country
            assertEquals(country, prefsCountry)
            testDataStore.edit { it.clear() }
        }
    }

    @AfterEach
    fun cleanUp() {
        testScope.cancel()
    }

}