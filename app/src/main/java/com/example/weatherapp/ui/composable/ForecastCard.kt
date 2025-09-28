package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.search.ForecastResult
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
            ForecastResult.Initial -> { /*TODO*/ }
            is ForecastResult.Failure -> { /*TODO*/ }
            is ForecastResult.ForecastSuccess -> { /*TODO*/ }
            is ForecastResult.Loading -> { /*TODO*/ }
        }
    }
}