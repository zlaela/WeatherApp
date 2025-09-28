package com.example.weatherapp.ui.utils

import com.example.weatherapp.R

fun getWeatherIconResource(iconCode: String): Int = when (iconCode) {
    "01d" -> R.drawable.icn_01d
    "02d" -> R.drawable.icn_02d
    "03d" -> R.drawable.icn_03d
    "04d" -> R.drawable.icn_04d
    "09d" -> R.drawable.icn_09d
    "10d" -> R.drawable.icn_10d
    "11d" -> R.drawable.icn_11d
    "13d" -> R.drawable.icn_13d
    "50d" -> R.drawable.icn_50d
    "01n" -> R.drawable.icn_01n
    "02n" -> R.drawable.icn_02n
    "03n" -> R.drawable.icn_03n
    "04n" -> R.drawable.icn_04n
    "09n" -> R.drawable.icn_09n
    "10n" -> R.drawable.icn_10n
    "11n" -> R.drawable.icn_11n
    "13n" -> R.drawable.icn_13n
    "50n" -> R.drawable.icn_50n
    else -> R.drawable.icn_10d
}