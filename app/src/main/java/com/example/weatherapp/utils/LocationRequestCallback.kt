package com.example.weatherapp.utils

import android.location.Location

interface LocationRequestCallback {
    fun onCanUseLocation()
    fun onLastLocationFound(location: Location)
    fun onLastLocationNotFound()
    fun onLocationPermissionsNotGranted()
}