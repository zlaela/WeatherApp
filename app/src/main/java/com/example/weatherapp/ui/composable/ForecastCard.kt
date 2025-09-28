package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.domain.DayNightForecast
import com.example.data.domain.Forecast
import com.example.data.search.ForecastResult
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags
import com.example.weatherapp.ui.utils.getWeatherIconResource

@Composable
fun ForecastCard(
    forecastStates: State<ForecastResult>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        locationForecasts.forEach { forecast ->
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
private fun DayNightForecastCard(dayNightForecast: DayNightForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Day forecast
        dayNightForecast.dayForecast?.let { dayForecast ->
            DayNightItem(
                forecast = dayForecast,
                modifier = Modifier.weight(1f)
            )
        }

        // Divider
        if (dayNightForecast.dayForecast != null && dayNightForecast.nightForecast != null) {
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Night forecast
        dayNightForecast.nightForecast?.let { nightForecast ->
            DayNightItem(
                forecast = nightForecast,
                modifier = Modifier.weight(1f),
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DayNightItem(
    forecast: Forecast,
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.inversePrimary
) {
    val iconResource = remember { getWeatherIconResource(forecast.icon) }
    
    // Use appropriate text colors based on background
    val textColor = if (backgroundColor == MaterialTheme.colorScheme.primary) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp
                ),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = forecast.description.capitalize(Locale.current),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Temperature range
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TempRange( R.string.high, forecast.tempMax.toInt(), textColor)

                Image(
                    painter = painterResource(id = iconResource),
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier.weight(1f).fillMaxHeight().aspectRatio(1f)
                )
                TempRange(R.string.low, forecast.tempMin.toInt(), textColor)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Humidity and chance of rain
            HumidityRain(forecast.humidity, forecast.chanceOfRain.toInt(), textColor)
        }
    }
}

@Composable
private fun TempRange(
    tempRes: Int,
    temp: Int,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.SpaceAround) {
        Text(
            text = stringResource(tempRes),
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )

        Text(
            text = stringResource(R.string.temp_f, temp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color =textColor
        )
    }
}

@Composable
private fun HumidityRain(
    humidity: Int,
    rain: Int,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
    ) {
        Text(
            text = stringResource(R.string.humidity_pct, humidity),
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
        Text(
            text = stringResource(R.string.chance_rain_pct, rain),
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}
