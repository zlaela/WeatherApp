package com.example.data

import java.io.InputStreamReader

class FileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this::class.java.getResourceAsStream("/$path"))
        content = reader.readText()
        reader.close()
    }
}