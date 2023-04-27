package com.example.data.repository

import com.example.data.domain.City
import com.example.data.exception.DataStoreUpdateException
import com.example.data.store.DataStoreState
import com.example.data.store.PreferencesDataSource
import com.example.data.store.UserPreferences

class DataStoreRepository(
    private val dataSource: PreferencesDataSource,
) {
    suspend fun getStoredCity(): City? = when (val prefs = getPreferences()) {
        is DataStoreState.GetPreferencesSuccess -> prefs.city
        else -> null
    }

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

    private fun makeCity(prefs: UserPreferences): City? =
        if (prefs.name.isNullOrBlank()) {
            null
        } else {
            City(prefs.country ?: "", "", prefs.name, prefs.lat ?: 0.0, prefs.lon ?: 0.0)
        }
}

