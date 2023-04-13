package com.example.data.di

import com.example.data.api.GeoApi
import com.example.data.api.WeatherApi
import com.example.data.repository.DataStoreRepository
import com.example.data.repository.SearchRepository
import com.example.data.repository.WeatherSearchRepository
import com.example.data.store.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGeoSearchRepository(
        geoApi: GeoApi
    ) = SearchRepository(geoApi)

    @Provides
    @Singleton
    fun provideWeatherSearchRepository(
        weatherApi: WeatherApi
    ) = WeatherSearchRepository(weatherApi)

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataSource: PreferencesDataSource
    ) = DataStoreRepository(dataSource)
}