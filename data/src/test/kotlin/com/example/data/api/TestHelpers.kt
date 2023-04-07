package com.example.data.api

import com.example.data.FileReader
import com.squareup.moshi.Moshi

object TestHelpers {
    private val moshi = Moshi.Builder().build()

    fun <T> getWeatherResponse(responseClass: Class<T>, path: String): T {
        val jsonAdapter = moshi.adapter(responseClass)
        val jsonString = FileReader(path).content
        return jsonAdapter.fromJson(jsonString)!!
    }
}