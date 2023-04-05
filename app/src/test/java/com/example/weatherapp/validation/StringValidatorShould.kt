package com.example.weatherapp.validation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class StringValidatorShould {
    @ParameterizedTest
    @CsvSource(
        value = [
            "'', false",
            "'2', false",
            "'#$', false",
            "'2A', false",
            "'1N', false"
        ]
    )
    fun `notify when search string is invalid`(someCity: String, isValid: Boolean) {
        val validator = StringValidator()
        // Returns expected result when invalid term is evaluated
        assertEquals(validator.validate(someCity), isValid)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'Denver', true",
            "'60652', true",
            "'San Antonio', true",
            "'Mbanza-Ngungu', true",
            "'Saint-Chély-d'Apcher', true"
        ]
    )
    fun `notify when search string is valid`(someCity: String, isValid: Boolean) {
        val validator = StringValidator()
        // Returns expected result when invalid term is evaluated
        assertEquals(validator.validate(someCity), isValid)
    }
}