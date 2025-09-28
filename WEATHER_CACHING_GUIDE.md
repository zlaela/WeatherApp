# Weather Data and Icon Caching Implementation

This guide explains how the weather data and icon caching system works in the WeatherApp.

## Overview

The caching system provides:
- **Weather Data Caching**: Current weather and forecast data are cached locally using Room database
- **Weather Icon Caching**: Weather icons are downloaded and cached locally for offline use
- **Automatic Cache Management**: Expired cache is automatically cleaned up
- **Fallback Support**: If API fails, cached data is returned even if expired

## Architecture

### Database Entities

1. **CurrentWeatherEntity**: Stores current weather data for cities
2. **ForecastWeatherEntity**: Stores forecast data for cities  
3. **WeatherIconEntity**: Stores weather icon metadata and local file paths

### Key Components

1. **CachedWeatherRepository**: Main repository that handles caching logic
2. **WeatherIconCacheManager**: Manages weather icon downloading and caching
3. **WeatherIconUtils**: UI utility for using cached icons in Composables

## Usage

### Weather Data Caching

The `CachedWeatherRepository` automatically handles caching:

```kotlin
// Weather data is automatically cached when fetched
val weatherResult = weatherRepository.getCurrentWeather(city)
val forecastResult = weatherRepository.getForecast(city)
```

**Cache Duration**: 10 minutes for weather data, 7 days for icons

### Weather Icon Caching

#### In Composables

```kotlin
@Composable
fun WeatherIcon(iconCode: String) {
    val iconResource = rememberWeatherIconResource(iconCode)
    
    Image(
        painter = painterResource(id = iconResource),
        contentDescription = "Weather icon"
    )
}
```

#### Manual Icon Caching

```kotlin
// Pre-cache a single icon
iconCacheManager.getWeatherIcon("01d")

// Pre-cache multiple icons
iconCacheManager.preCacheIcons(listOf("01d", "02d", "03d"))
```

### Cache Management

```kotlin
// Clear expired cache (called automatically)
cachedWeatherRepository.clearExpiredCache()

// Clear all cache
cachedWeatherRepository.clearAllCache()
```

## Implementation Details

### Database Schema

```sql
-- Current weather cache
CREATE TABLE current_weather (
    cityName TEXT PRIMARY KEY,
    lat REAL,
    lon REAL,
    tempMin REAL,
    tempMax REAL,
    temp REAL,
    feelsLike REAL,
    icon TEXT,
    description TEXT,
    cachedAt INTEGER
);

-- Forecast cache  
CREATE TABLE forecast_weather (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cityName TEXT,
    forecastDateTimeUtc INTEGER,
    forecastDateTime TEXT,
    tempMin REAL,
    tempMax REAL,
    partOfDay TEXT,
    icon TEXT,
    condition TEXT,
    description TEXT,
    cachedAt INTEGER
);

-- Weather icons cache
CREATE TABLE weather_icons (
    iconCode TEXT PRIMARY KEY,
    iconUrl TEXT,
    localPath TEXT,
    cachedAt INTEGER
);
```

### Icon Mapping

The system maps OpenWeatherMap icon codes to local drawable resources:

| Icon Code | Drawable Resource |
|-----------|-------------------|
| 01d, 02d  | R.drawable.sunny  |
| 03d, 04d, 04n, 03n, 02n | R.drawable.cloudy |
| 09d, 10n, 09n | R.drawable.rainy |
| 10d | R.drawable.rainy_sunny |
| 11d, 11n | R.drawable.thunder_lightning |
| 13d, 13n | R.drawable.snow |
| 50d, 50n | R.drawable.fog |
| 01n | R.drawable.clear |

### Cache Strategy

1. **Cache-First**: Always check cache before API call
2. **Background Refresh**: Cache is updated in background when expired
3. **Offline Fallback**: Return cached data if API fails
4. **Automatic Cleanup**: Expired cache is automatically removed

## Benefits

1. **Offline Support**: App works without internet connection
2. **Faster Loading**: Cached data loads instantly
3. **Reduced API Calls**: Saves bandwidth and API quota
4. **Better UX**: Smooth experience even with poor connectivity
5. **Icon Optimization**: Icons are downloaded once and reused

## Configuration

### Cache Durations

- Weather Data: 10 minutes
- Weather Icons: 7 days
- Database: Room with automatic migration

### Storage

- Icons stored in app cache directory
- Database stored in app data directory
- Automatic cleanup on app uninstall

## Testing

The caching system can be tested by:
1. Fetching weather data with internet
2. Turning off internet and checking if data still loads
3. Verifying icons are cached and displayed correctly
4. Testing cache expiration and refresh behavior
