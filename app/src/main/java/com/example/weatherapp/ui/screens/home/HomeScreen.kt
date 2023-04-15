package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.weatherapp.ui.TestTags
import com.example.weatherapp.ui.composable.AppTopAppBar
import com.example.weatherapp.viewmodel.CitySearchViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    citySearchViewModel: CitySearchViewModel = hiltViewModel(),
) {
    val cityStates: State<SearchState> =
        citySearchViewModel.searchLiveData.observeAsState(SearchState.Initial)

    val onCitySelected: (City) -> Unit = { selectedCity ->
        // TODO: Get weather for this city
    }

    Scaffold(
        modifier = Modifier
            .testTag(TestTags.MAIN)
            .background(colors.background),
        topBar = { AppTopAppBar() },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            CitySearchBarAndResults(
                citySearchViewModel = citySearchViewModel,
                cityStates = cityStates,
                onCitySelected = onCitySelected
            )
        }
    }
}