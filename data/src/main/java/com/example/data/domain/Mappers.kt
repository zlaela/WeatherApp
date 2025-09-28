package com.example.data.domain

import com.example.data.api.response.city.CityItem
import com.example.data.api.response.weather.CurrentWeatherResponse
import com.example.data.api.response.weather.FiveDayForecastResponse
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.component2

fun List<CityItem>.mapToCitiesList(): List<City> =
    this.map {
        it.mapToCity()
    }

fun CityItem.mapToCity(): City {
    this.run {
        println("City item is $this")
        return if (name == null) {
            City("", "", "Unknown City", 0.0, 0.0)
        } else {
            City(country ?: "", state ?: "", name, lat ?: 0.0, lon ?: 0.0)
        }
    }
}

fun CurrentWeatherResponse.mapToCurrentWeather(): CurrentWeather =
    CurrentWeather(
        this.name ?: "",
        this.coord?.lat ?: 0.0,
        this.coord?.lon ?: 0.0,
        this.main?.tempMin ?: 0.0,
        this.main?.tempMax ?: 0.0,
        this.main?.temp ?: 0.0,
        this.main?.feelsLike ?: 0.0,
        this.weather?.first()?.icon ?: "",
        this.weather?.first()?.description ?: ""
    )

fun FiveDayForecastResponse.mapToForecast(): List<Forecast> =
    with(mutableListOf<Forecast>()) {
        this@mapToForecast.forecasts?.forEach {
            add(Forecast(
                it.dt ?: 0,
                it.dtTxt ?: "",
                it.main?.tempMin ?: 0.0,
                it.main?.tempMax ?: 0.0,
                it.partOfDay?.partOfDay ?: "",
                it.weather?.first()?.icon ?: "",
                it.weather?.first()?.main ?: "",
                it.weather?.first()?.description ?: "",
                it.main?.humidity?: 0,
                it.probabilityOfPrecip ?: 0.0
            ))
        }
        this
    }.toList()


fun List<Forecast>.mapToDayNightForecast(): List<DayNightForecast> =
    this.groupBy { forecast ->
        val instant = Instant.ofEpochSecond(forecast.forecastDateTimeUtc.toLong())
        instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }
        .filterKeys { date ->
            // Remove today from the forecast
            date != java.time.LocalDate.now()
        }
        .map { (date, forecastMap) ->
            val dayForecasts = forecastMap.filter { it.partOfDay == "d" }
            val nightForecasts = forecastMap.filter { it.partOfDay == "n" }

            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, EEEE", Locale.getDefault())
            val formattedDate = date.format(dateFormatter)

            DayNightForecast(
                dateString = formattedDate,
                localDate = date,
                dayForecast = dayForecasts.maxByOrNull { it.tempMax }, // Warmest day
                nightForecast = nightForecasts.minByOrNull { it.tempMin }, // Coolest night
                dayHigh = dayForecasts.maxOfOrNull { it.tempMax } ?: 0.0, // Highest day temp
                dayLow = dayForecasts.minOfOrNull { it.tempMin } ?: 0.0,  // Lowest day temp
                nightHigh = nightForecasts.maxOfOrNull { it.tempMax } ?: 0.0, // Highest night temp
                nightLow = nightForecasts.minOfOrNull { it.tempMin } ?: 0.0   // Lowest night temp
            )
        }.sortedBy { it.localDate }
