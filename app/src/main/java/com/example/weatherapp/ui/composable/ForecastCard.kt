package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.domain.DayNightForecast
import com.example.data.search.ForecastResult
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags

@Composable
fun ForecastCard(
    forecastStates: State<ForecastResult>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.CITY_FORECAST)
    ) {
        when (val state = forecastStates.value) {
            ForecastResult.Initial -> InitialForecastState()
            is ForecastResult.Failure -> ErrorForecastState(state.reason)
            is ForecastResult.ForecastSuccess -> { ForecastState(state.locationForecast) }
            is ForecastResult.Loading -> LoadingForecastState()
        }
    }
}

@Composable
private fun InitialForecastState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.forecast_pending),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LoadingForecastState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(32.dp),
                color = MaterialTheme.colorScheme.onBackground,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.loading_forecast),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ErrorForecastState(reason: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.error),
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.failed_to_load_forecast),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ForecastState(locationForecasts: List<DayNightForecast>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(locationForecasts.size) { idx ->
            val forecast = locationForecasts[idx]
            // date/ day of week
            Text(
                text = forecast.dateString,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            DayNightForecastCard(dayNightForecast = forecast)
        }
    }
}

@Composable
fun DayNightForecastCard(dayNightForecast: DayNightForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Day forecast
        dayNightForecast.dayForecast?.let { dayForecast ->
            Text("$dayForecast", modifier = Modifier.width(50.dp))
        }

        // Divider
        if (dayNightForecast.dayForecast != null && dayNightForecast.nightForecast != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Night forecast
        dayNightForecast.nightForecast?.let { nightForecast ->
            Text("$nightForecast", modifier = Modifier.width(50.dp))
        }
    }
}