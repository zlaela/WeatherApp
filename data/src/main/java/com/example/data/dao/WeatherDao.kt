package com.example.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM current_weather WHERE cityName = :cityName")
    suspend fun getCurrentWeather(cityName: String): CurrentWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentWeatherEntity)

    @Query("DELETE FROM current_weather WHERE cityName = :cityName")
    suspend fun deleteCurrentWeather(cityName: String)

    @Query("SELECT * FROM current_weather WHERE cachedAt > :timestamp")
    suspend fun getValidCurrentWeather(timestamp: Long): List<CurrentWeatherEntity>

    @Query("DELETE FROM current_weather WHERE cachedAt < :timestamp")
    suspend fun deleteExpiredCurrentWeather(timestamp: Long)
}

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast_weather WHERE cityName = :cityName ORDER BY forecastDateTimeUtc ASC")
    suspend fun getForecast(cityName: String): List<ForecastWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: List<ForecastWeatherEntity>)

    @Query("DELETE FROM forecast_weather WHERE cityName = :cityName")
    suspend fun deleteForecast(cityName: String)

    @Query("SELECT * FROM forecast_weather WHERE cachedAt > :timestamp")
    suspend fun getValidForecast(timestamp: Long): List<ForecastWeatherEntity>

    @Query("DELETE FROM forecast_weather WHERE cachedAt < :timestamp")
    suspend fun deleteExpiredForecast(timestamp: Long)
}

@Dao
interface WeatherIconDao {
    @Query("SELECT * FROM weather_icons WHERE iconCode = :iconCode")
    suspend fun getWeatherIcon(iconCode: String): WeatherIconEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherIcon(icon: WeatherIconEntity)

    @Query("DELETE FROM weather_icons WHERE iconCode = :iconCode")
    suspend fun deleteWeatherIcon(iconCode: String)

    @Query("SELECT * FROM weather_icons WHERE cachedAt > :timestamp")
    suspend fun getValidWeatherIcons(timestamp: Long): List<WeatherIconEntity>

    @Query("DELETE FROM weather_icons WHERE cachedAt < :timestamp")
    suspend fun deleteExpiredWeatherIcons(timestamp: Long)

    @Query("SELECT * FROM weather_icons")
    suspend fun getAllWeatherIcons(): List<WeatherIconEntity>
}
