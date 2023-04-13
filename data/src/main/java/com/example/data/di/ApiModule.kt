package com.example.data.di

import com.example.data.api.GeoApi
import com.example.data.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create()

    @Provides
    @Singleton
    fun provideGeoApi(retrofit: Retrofit): GeoApi = retrofit.create()
}