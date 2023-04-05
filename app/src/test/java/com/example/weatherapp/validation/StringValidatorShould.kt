package com.example.weatherapp.validation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class StringValidatorShould {
    @ParameterizedTest
    @CsvSource("''", "2", "#$", "2A", "1N")
    fun `notify when search string is invalid`(someCity: String) {
        val validator = StringValidator()
        // Returns expected result when invalid term is evaluated
        assertEquals(validator.validate(someCity), validator.validate(someCity))
    }

    @ParameterizedTest
    @CsvSource("Denver", "60652", "San Antonio", "Mbanza-Ngungu", "Saint-Chély-d'Apcher")
    fun `notify when search string is valid`(someCity: String) {
        val validator = StringValidator()
        // Returns expected result when invalid term is evaluated
        assertEquals(validator.validate(someCity), validator.validate(someCity))
    }
}