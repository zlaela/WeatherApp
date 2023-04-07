package com.example.weatherapp

import java.io.InputStreamReader

// TODO: Move to a common testing package
// This is duplicated here and in the data package and should be consolidated into a shared package for all tests
class FileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this::class.java.getResourceAsStream("/$path"))
        content = reader.readText()
        reader.close()
    }
}