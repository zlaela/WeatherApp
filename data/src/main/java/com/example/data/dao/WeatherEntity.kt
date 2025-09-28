package com.example.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey
    val cityName: String,
    val lat: Double,
    val lon: Double,
    val tempMin: Double,
    val tempMax: Double,
    val temp: Double,
    val feelsLike: Double,
    val icon: String,
    val description: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "forecast_weather")
data class ForecastWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val forecastDateTimeUtc: Int,
    val forecastDateTime: String,
    val tempMin: Double,
    val tempMax: Double,
    val partOfDay: String,
    val icon: String,
    val condition: String,
    val description: String,
    val humidity: Int,
    val chanceOfRain: Double,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "weather_icons")
data class WeatherIconEntity(
    @PrimaryKey
    val iconCode: String,
    val iconUrl: String,
    val localPath: String,
    val cachedAt: Long = System.currentTimeMillis()
)
