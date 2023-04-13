package com.example.weatherapp.di

import com.example.weatherapp.validation.StringValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class StringValidatorModule {

    @Provides
    fun provideStringValidator(): StringValidator = StringValidator()
}