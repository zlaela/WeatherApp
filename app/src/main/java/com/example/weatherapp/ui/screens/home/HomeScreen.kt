package com.example.weatherapp.ui.screens.home

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.data.domain.City
import com.example.data.search.ForecastResult
import com.example.data.search.SearchState
import com.example.data.search.WeatherResult
import com.example.weatherapp.ui.TestTags
import com.example.weatherapp.ui.composable.ForecastCard
import com.example.weatherapp.viewmodel.CitySearchViewModel
import com.example.weatherapp.viewmodel.WeatherSearchViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    weatherSearchViewModel: WeatherSearchViewModel = hiltViewModel(),
    citySearchViewModel: CitySearchViewModel = hiltViewModel(),
) {
    val onCitySelected: (City) -> Unit = { selectedCity ->
        weatherSearchViewModel.getWeatherFor(selectedCity)
        weatherSearchViewModel.getForecast(selectedCity)
    }

    val weatherStates = weatherSearchViewModel.weatherLiveData.observeAsState(WeatherResult.Initial)
    val forecastStates =
        weatherSearchViewModel.forecastLivedata.observeAsState(ForecastResult.Initial)
    val cityStates = citySearchViewModel.searchLiveData.observeAsState(SearchState.Initial)

    Home(
        onCitySelected = onCitySelected,
        cityStates = cityStates,
        weatherStates = weatherStates,
        forecastStates = forecastStates,
        doRefresh = { weatherSearchViewModel.refreshWeatherAndForecast() },
        topBar = {
            CitySearchAppBar(
                citySearchViewModel = citySearchViewModel, cityStates = cityStates
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    cityStates: State<SearchState>,
    weatherStates: State<WeatherResult>,
    forecastStates: State<ForecastResult>,
    onCitySelected: (City) -> Unit,
    topBar: @Composable () -> Unit,
    doRefresh: () -> Unit = {},
) {
    val state = rememberPullToRefreshState()
    val isRefreshing = weatherStates.value is WeatherResult.Loading ||
            forecastStates.value is ForecastResult.Loading

    val lazyListState = rememberLazyListState()

    val derived by remember { derivedStateOf { lazyListState.layoutInfo }}
    val derivedListOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset }}
    val derivedViewPort by remember { derivedStateOf { derived.viewportSize }}

    // Track scroll progress
    val scrollProgress = if (derived.visibleItemsInfo.isNotEmpty()) {
        val scrollOffset = derivedListOffset
        val viewportHeight = derivedViewPort.height
        if (viewportHeight > 0) {
            (scrollOffset.toFloat() / viewportHeight).coerceIn(0f, 1f)
        } else 0f
    } else 0f

    // Smooth continuous animation based on scroll progress
    val weatherCardHeight by animateFloatAsState(
        targetValue = 1f - (scrollProgress * 0.75f), // Shrink by 75% max
        animationSpec = tween(
            durationMillis = 10,
            easing = EaseInOutCubic
        ), label = "weatherCardHeight"
    )

    Scaffold(
        modifier = Modifier.testTag(TestTags.MAIN),
        topBar = { topBar() },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            PullToRefreshBox(
                modifier = Modifier,
                isRefreshing = isRefreshing,
                onRefresh = { doRefresh() },
                state = state
            ) {
                Column {
                    WeatherCard(
                        weatherStates,
                        animatedHeight = weatherCardHeight
                    )
                    LazyColumn(state = lazyListState) {
                        item {
                            ForecastCard(forecastStates = forecastStates)
                        }
                    }
                }
                // Keep this outside the refreshing check so it clears on click
                FetchedCitiesListDropdown(onCitySelected, cityStates)
            }
        }
    }
}