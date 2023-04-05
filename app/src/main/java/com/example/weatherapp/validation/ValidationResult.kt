package com.example.weatherapp.validation

sealed class ValidationResult {
    object Invalid: ValidationResult()
    object ValidZip: ValidationResult()
    object ValidCity: ValidationResult()
}
