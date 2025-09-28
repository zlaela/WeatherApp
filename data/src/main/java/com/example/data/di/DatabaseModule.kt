package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.cache.WeatherIconCacheManager
import com.example.data.dao.*
import com.example.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        )
        .fallbackToDestructiveMigration() // For development - remove in production
        .build()
    }

    @Provides
    fun provideCityDao(database: WeatherDatabase): CityDao = database.cityDao

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao

    @Provides
    fun provideForecastDao(database: WeatherDatabase): ForecastDao = database.forecastDao

    @Provides
    fun provideWeatherIconDao(database: WeatherDatabase): WeatherIconDao = database.weatherIconDao

    @Provides
    @Singleton
    fun provideWeatherIconCacheManager(
        @ApplicationContext context: Context,
        weatherIconDao: WeatherIconDao
    ): WeatherIconCacheManager = WeatherIconCacheManager(context, weatherIconDao)
}
