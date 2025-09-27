package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.search.WeatherResult
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags

@Composable
fun WeatherCard(
    weatherStates: State<WeatherResult>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.CITY_WEATHER),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        when (weatherStates.value) {
            WeatherResult.Initial -> InitialWeatherState()
            is WeatherResult.Failure -> {}
            is WeatherResult.ForecastSuccess -> {}
            is WeatherResult.Loading -> {}
            is WeatherResult.WeatherSuccess -> {}
        }
    }
}

@Composable
private fun InitialWeatherState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.search_for_a_location),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}