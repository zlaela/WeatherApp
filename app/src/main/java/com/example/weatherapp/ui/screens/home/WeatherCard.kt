package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.search.WeatherResult
import com.example.weatherapp.ui.TestTags

@Composable
fun WeatherCard(
    padding: PaddingValues,
    weatherStates: State<WeatherResult>,) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.CITY_WEATHER)
    ) {
        // TODO: show today's weather
    }
}