package com.example.data.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.data.dao.WeatherIconDao
import com.example.data.dao.WeatherIconEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherIconCacheManager @Inject constructor(
    private val context: Context,
    private val weatherIconDao: WeatherIconDao
) {
    companion object {
        private const val ICON_CACHE_DIR = "weather_icons"
        private const val ICON_CACHE_DURATION = 7 * 24 * 60 * 60 * 1000L // 7 days in milliseconds
        private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"
        private const val ICON_SIZE = "@2x.png"
    }

    private val cacheDir: File by lazy {
        File(context.cacheDir, ICON_CACHE_DIR).apply {
            if (!exists()) mkdirs()
        }
    }

    suspend fun getWeatherIcon(iconCode: String): String? = withContext(Dispatchers.IO) {
        try {
            // First check if we have a cached version
            val cachedIcon = weatherIconDao.getWeatherIcon(iconCode)
            if (cachedIcon != null && isIconValid(cachedIcon.cachedAt)) {
                val localFile = File(cachedIcon.localPath)
                if (localFile.exists()) {
                    return@withContext cachedIcon.localPath
                } else {
                    // File doesn't exist, remove from database
                    weatherIconDao.deleteWeatherIcon(iconCode)
                }
            }

            // Download and cache the icon
            downloadAndCacheIcon(iconCode)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun downloadAndCacheIcon(iconCode: String): String? = withContext(Dispatchers.IO) {
        try {
            val iconUrl = "$ICON_BASE_URL$iconCode$ICON_SIZE"
            val url = URL(iconUrl)
            val connection = url.openConnection()
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap != null) {
                val localFile = File(cacheDir, "$iconCode.png")
                val outputStream = FileOutputStream(localFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

                // Save to database
                val iconEntity = WeatherIconEntity(
                    iconCode = iconCode,
                    iconUrl = iconUrl,
                    localPath = localFile.absolutePath
                )
                weatherIconDao.insertWeatherIcon(iconEntity)

                localFile.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isIconValid(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt < ICON_CACHE_DURATION
    }

    suspend fun clearExpiredIcons() = withContext(Dispatchers.IO) {
        val expiredTimestamp = System.currentTimeMillis() - ICON_CACHE_DURATION
        val expiredIcons = weatherIconDao.getValidWeatherIcons(expiredTimestamp)
        
        // Delete files
        expiredIcons.forEach { icon ->
            File(icon.localPath).delete()
        }
        
        // Delete from database
        weatherIconDao.deleteExpiredWeatherIcons(expiredTimestamp)
    }

    suspend fun clearAllIcons() = withContext(Dispatchers.IO) {
        val allIcons = weatherIconDao.getAllWeatherIcons()
        
        // Delete files
        allIcons.forEach { icon ->
            File(icon.localPath).delete()
        }
        
        // Clear database
        weatherIconDao.deleteExpiredWeatherIcons(0)
    }
}
