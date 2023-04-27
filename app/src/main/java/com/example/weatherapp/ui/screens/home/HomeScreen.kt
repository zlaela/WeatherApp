package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.data.domain.City
import com.example.data.search.SearchState
import com.example.data.search.WeatherResult
import com.example.weatherapp.ui.TestTags
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
    }

    val weatherStates = weatherSearchViewModel.weatherLiveData.observeAsState(WeatherResult.Initial)
    val cityStates = citySearchViewModel.searchLiveData.observeAsState(SearchState.Initial)

    Home(
        onCitySelected = onCitySelected,
        cityStates = cityStates,
        weatherStates = weatherStates,
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
    onCitySelected: (City) -> Unit,
    topBar: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(TestTags.MAIN),
        topBar = { topBar() },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(colors.primary)
        ) {
            WeatherCard(padding, weatherStates)
            FetchedCitiesListDropdown(onCitySelected, cityStates)
        }
    }
}
