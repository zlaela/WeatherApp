package com.example.weatherapp

import com.squareup.moshi.Moshi

// TODO: Move to a common testing package
// This is duplicated here and in the data package and should be consolidated into a shared package for all tests
object TestHelpers {
    private val moshi = Moshi.Builder().build()

    fun <T> getWeatherResponse(responseClass: Class<T>, path: String): T {
        val jsonAdapter = moshi.adapter(responseClass)
        val jsonString = FileReader(path).content
        return jsonAdapter.fromJson(jsonString)!!
    }
}