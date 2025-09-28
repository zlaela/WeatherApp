package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.data.api.response.city.LocalNames
import com.example.data.dao.*

@Database(
    entities = [
        CityEntity::class,
        CurrentWeatherEntity::class,
        ForecastWeatherEntity::class,
        WeatherIconEntity::class
    ], 
    version = 2, 
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val cityDao: CityDao
    abstract val weatherDao: WeatherDao
    abstract val forecastDao: ForecastDao
    abstract val weatherIconDao: WeatherIconDao
}

class CityConverter {
    @TypeConverter
    fun namesList(localNames: LocalNames?): String? {
        return localNames?.toString()
    }
}