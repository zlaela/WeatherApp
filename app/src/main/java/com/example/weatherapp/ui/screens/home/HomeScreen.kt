package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
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
    val forecastStates = weatherSearchViewModel.forecastLivedata.observeAsState(ForecastResult.Initial)
    val cityStates = citySearchViewModel.searchLiveData.observeAsState(SearchState.Initial)

    Home(
        onCitySelected = onCitySelected,
        cityStates = cityStates,
        weatherStates = weatherStates,
        forecastStates = forecastStates,
        topBar = {
            CitySearchAppBar(
                citySearchViewModel = citySearchViewModel, cityStates = cityStates
            )
        }
    )
}

@Composable
fun Home(
    cityStates: State<SearchState>,
    weatherStates: State<WeatherResult>,
    forecastStates: State<ForecastResult>,
    onCitySelected: (City) -> Unit,
    topBar: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(TestTags.MAIN),
        topBar = { topBar() },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp)
        ) {
            WeatherCard(weatherStates)
            ForecastCard(forecastStates = forecastStates)
            FetchedCitiesListDropdown(onCitySelected, cityStates)
        }
    }
}
