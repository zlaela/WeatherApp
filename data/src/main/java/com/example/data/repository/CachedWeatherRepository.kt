package com.example.data.repository

import com.example.data.api.WeatherApi
import com.example.data.cache.WeatherIconCacheManager
import com.example.data.dao.ForecastDao
import com.example.data.dao.WeatherDao
import com.example.data.dao.ForecastWeatherEntity
import com.example.data.dao.CurrentWeatherEntity
import com.example.data.domain.City
import com.example.data.domain.CurrentWeather
import com.example.data.domain.Forecast
import com.example.data.domain.mapToCurrentWeather
import com.example.data.domain.mapToDayNightForecast
import com.example.data.domain.mapToForecast
import com.example.data.search.ForecastResult
import com.example.data.search.WeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao,
    private val iconCacheManager: WeatherIconCacheManager
) : WeatherRepository {

    companion object {
        private const val CACHE_DURATION = 10 * 60 * 1000L // 10 minutes in milliseconds
    }

    override suspend fun getCurrentWeather(someCity: City): WeatherResult = withContext(Dispatchers.IO) {
        try {
            // Check cache first
            val cachedWeather = weatherDao.getCurrentWeather(someCity.name)
            if (cachedWeather != null && isCacheValid(cachedWeather.cachedAt)) {
                val currentWeather = cachedWeather.toCurrentWeather()
                // Pre-cache the icon
                iconCacheManager.getWeatherIcon(cachedWeather.icon)
                return@withContext WeatherResult.WeatherSuccess(currentWeather)
            }

            // Fetch from API
            val apiResponse = weatherApi.getCurrentWeather(someCity.name).await()
            val currentWeather = apiResponse.mapToCurrentWeather()

            // Cache the result
            val weatherEntity = currentWeather.toEntity()
            weatherDao.insertCurrentWeather(weatherEntity)

            // Pre-cache the icon
            iconCacheManager.getWeatherIcon(currentWeather.icon)

            WeatherResult.WeatherSuccess(currentWeather)
        } catch (throwable: Throwable) {
            // If API fails, try to return cached data even if expired
            val cachedWeather = weatherDao.getCurrentWeather(someCity.name)
            if (cachedWeather != null) {
                val currentWeather = cachedWeather.toCurrentWeather()
                iconCacheManager.getWeatherIcon(cachedWeather.icon)
                WeatherResult.WeatherSuccess(currentWeather)
            } else {
                WeatherResult.Failure(reasonFor(throwable))
            }
        }
    }

    override suspend fun getForecast(someCity: City): ForecastResult = withContext(Dispatchers.IO) {
        try {
            // Check cache first
            val cachedForecast = forecastDao.getForecast(someCity.name)
            if (cachedForecast.isNotEmpty() && isCacheValid(cachedForecast.first().cachedAt)) {
                val forecast = cachedForecast.map { it.toForecast() }
                // Pre-cache all icons
                forecast.forEach { forecastItem ->
                    iconCacheManager.getWeatherIcon(forecastItem.icon)
                }
                return@withContext ForecastResult.ForecastSuccess(forecast.mapToDayNightForecast())
            }

            // Fetch from API
            val apiResponse = weatherApi.getForecast(someCity.lat, someCity.lon).await()
            val forecast = apiResponse.mapToForecast()

            // Cache the result
            val forecastEntities = forecast.map { it.toEntity(someCity.name) }
            forecastDao.insertForecast(forecastEntities)

            // Pre-cache all icons
            forecast.forEach { forecastItem ->
                iconCacheManager.getWeatherIcon(forecastItem.icon)
            }

            ForecastResult.ForecastSuccess(forecast.mapToDayNightForecast())
        } catch (throwable: Throwable) {
            // If API fails, try to return cached data even if expired
            val cachedForecast = forecastDao.getForecast(someCity.name)
            if (cachedForecast.isNotEmpty()) {
                val forecast = cachedForecast.map { it.toForecast() }
                forecast.forEach { forecastItem ->
                    iconCacheManager.getWeatherIcon(forecastItem.icon)
                }
                ForecastResult.ForecastSuccess(forecast.mapToDayNightForecast())
            } else {
                ForecastResult.Failure(reasonFor(throwable))
            }
        }
    }

    private fun isCacheValid(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt < CACHE_DURATION
    }

    suspend fun clearExpiredCache() = withContext(Dispatchers.IO) {
        val expiredTimestamp = System.currentTimeMillis() - CACHE_DURATION
        weatherDao.deleteExpiredCurrentWeather(expiredTimestamp)
        forecastDao.deleteExpiredForecast(expiredTimestamp)
        iconCacheManager.clearExpiredIcons()
    }

    suspend fun clearAllCache() = withContext(Dispatchers.IO) {
        // Note: This would need to be implemented in the DAOs
        // For now, we'll just clear expired cache
        clearExpiredCache()
    }
}

// Extension functions for mapping between entities and domain objects
private fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        city = cityName,
        lat = lat,
        lon = lon,
        tempMin = tempMin,
        tempMax = tempMax,
        temp = temp,
        feelsLike = feelsLike,
        icon = icon,
        description = description
    )
}

private fun CurrentWeather.toEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        cityName = city,
        lat = lat,
        lon = lon,
        tempMin = tempMin,
        tempMax = tempMax,
        temp = temp,
        feelsLike = feelsLike,
        icon = icon,
        description = description
    )
}

private fun ForecastWeatherEntity.toForecast(): Forecast {
    return Forecast(
        forecastDateTimeUtc = forecastDateTimeUtc,
        forecastDateTime = forecastDateTime,
        tempMin = tempMin,
        tempMax = tempMax,
        partOfDay = partOfDay,
        icon = icon,
        condition = condition,
        description = description,
        humidity = humidity,
        chanceOfRain = chanceOfRain,
    )
}

private fun Forecast.toEntity(cityName: String): ForecastWeatherEntity {
    return ForecastWeatherEntity(
        cityName = cityName,
        forecastDateTimeUtc = forecastDateTimeUtc,
        forecastDateTime = forecastDateTime,
        tempMin = tempMin,
        tempMax = tempMax,
        partOfDay = partOfDay,
        icon = icon,
        condition = condition,
        description = description,
        humidity = humidity,
        chanceOfRain = chanceOfRain,
    )
}
