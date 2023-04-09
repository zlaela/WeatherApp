package com.example.data.store

import com.example.data.domain.City

sealed class DataStoreState {
    object FailedToGetPreferences : DataStoreState()
    object SuccessfullySetPreferences : DataStoreState()
    data class FailedToSetPreferences(val pref: String) : DataStoreState()
    data class GetPreferencesSuccess(val city: City?, val country: String?) : DataStoreState()
}