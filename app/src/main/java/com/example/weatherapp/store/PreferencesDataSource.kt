package com.example.weatherapp.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.data.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    val preferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // data read/write error
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            mapPreferences(prefs)
        }

    suspend fun updateCountry(country: String) =
        try {
            dataStore.edit { prefs ->
                prefs[PreferenceKeys.COUNTRY] = country
            }
        } catch (e: IOException) {
            // TODO: change to prefs update exception
        }

    suspend fun updateCity(city: City) =
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.CITY_NAME] = city.name
            prefs[PreferenceKeys.CITY_LAT] = city.lat
            prefs[PreferenceKeys.CITY_LON] = city.lon
            prefs[PreferenceKeys.CITY_ID] = city.id ?: -1
        }

    suspend fun getPreferences() =
        try {
            mapPreferences(dataStore.data.first().toPreferences())
        } catch (e: IOException) {
            // TODO: change to prefs update exception
        }

    private fun mapPreferences(preferences: Preferences): UserPreferences {
        val country = preferences[PreferenceKeys.COUNTRY] ?: ""
        val cityId = preferences[PreferenceKeys.CITY_ID] ?: -1
        val cityLat = preferences[PreferenceKeys.CITY_LAT] ?: 0.0
        val cityLon = preferences[PreferenceKeys.CITY_LON] ?: 0.0
        val cityName = preferences[PreferenceKeys.CITY_NAME] ?: ""

        return UserPreferences(
            City(cityName, cityLat, cityLon, cityId),
            country
        )
    }

    private object PreferenceKeys {
        val COUNTRY = stringPreferencesKey("country")
        val CITY_ID = intPreferencesKey("id")
        val CITY_LAT = doublePreferencesKey("lat")
        val CITY_LON = doublePreferencesKey("lon")
        val CITY_NAME = stringPreferencesKey("name")
    }
}