package com.example.weatherapp.validation

import java.util.regex.Pattern

class StringValidator {
    private val cityPattern = Pattern.compile(CITY_REGEX)
    private val zipPattern = Pattern.compile(ZIP_REGEX)

    fun validate(someCity: String): ValidationResult =
        if (someCity.trim().isBlank()) {
            ValidationResult.Invalid
        } else {
            if (cityPattern.matcher(someCity).matches()) {
                ValidationResult.ValidCity
            } else if (zipPattern.matcher(someCity).matches()) {
                ValidationResult.ValidZip
            } else {
                ValidationResult.Invalid
            }
        }

    companion object {
        /**
         * TODO: better regex
         * These likely don't account for all possible city/zip formats.
         * With more time, these would be better, or I'd find some library to sanitize
         * https://stackoverflow.com/questions/8923729/checking-for-diacritics-with-a-regular-expression
         */
        private const val ZIP_REGEX = """\d{2,64}"""
        private const val CITY_REGEX = """[a-zA-z '\-^.\p{L}*${'$'}]{2,64}"""
    }
}
