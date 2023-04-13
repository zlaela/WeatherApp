package com.example.data.di

import com.example.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * This is an interface because it binds an implementation to the interface
 * Could also be an abstract class
 */
@Module
@InstallIn(ViewModelComponent::class)
interface WeatherRepositoryModule {

    @Binds
    fun bindWeatherRepository(weatherRepository: WeatherRepository): WeatherRepository
}