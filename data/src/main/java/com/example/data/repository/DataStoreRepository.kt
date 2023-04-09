package com.example.data.repository

import com.example.data.domain.City
import com.example.data.exception.DataStoreUpdateException
import com.example.data.store.PreferencesDataSource
import com.example.data.store.DataStoreState
import com.example.data.store.UserPreferences

class DataStoreRepository(
    private val dataSource: PreferencesDataSource,
) {
    suspend fun getPreferences(): DataStoreState {
        return try {
            val prefs = dataSource.getPreferences()
            val city = makeCity(prefs)
            DataStoreState.GetPreferencesSuccess(city, prefs.country)
        } catch (ex: DataStoreUpdateException) {
            DataStoreState.FailedToGetPreferences
        }
    }

    suspend fun updateCity(city: City): DataStoreState =
        try {
            dataSource.updateCity(city)
            DataStoreState.SuccessfullySetPreferences
        } catch (e: DataStoreUpdateException) {
            DataStoreState.FailedToSetPreferences(city.name)
        }

    suspend fun updateCountry(country: String): DataStoreState =
        try {
            dataSource.updateCountry(country)
            DataStoreState.SuccessfullySetPreferences
        } catch (e: DataStoreUpdateException) {
            DataStoreState.FailedToSetPreferences(country)
        }

    // TODO: gross. Do better
    private fun makeCity(prefs: UserPreferences) : City? =
        listOfNotNull(prefs.name, prefs.lat, prefs.lon)
            .takeIf { it.isNotEmpty() }
            ?.let { nnPrefs ->
                City(nnPrefs[0] as String,
                    nnPrefs[1] as Double,
                    nnPrefs[2] as Double,
                    prefs.id)
            }
}

