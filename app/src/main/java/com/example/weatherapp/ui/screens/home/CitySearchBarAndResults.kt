package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.data.domain.City
import com.example.data.search.SearchState
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags
import com.example.weatherapp.ui.composable.FullScreenCenteredSpinner
import com.example.weatherapp.ui.composable.SearchBar
import com.example.weatherapp.viewmodel.CitySearchViewModel

@Composable
fun CitySearchBarAndResults(
    citySearchViewModel: CitySearchViewModel,
    cityStates: State<SearchState>,
    onCitySelected: (City) -> Unit,
) {
    var error by remember { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var textFieldContents by remember { mutableStateOf("") }

    val search: (String) -> Unit = { textFieldText ->
        citySearchViewModel.search(textFieldText)
    }
    val hint = stringResource(
        if (error) {
            R.string.hint_invalid_location
        } else {
            R.string.hint_enter_location
        }
    )

    Column {
        SearchBar(enabled = enabled,
            searchHint = hint,
            isError = error,
            trailingIcon = {
                Icon(
                    modifier = Modifier.testTag(TestTags.SEARCH_ICON),
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.content_description_city_search_icon)
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.testTag(TestTags.LOCATION_ICON),
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.content_description_current_location_icon)
                )
            },
            onLeadingIconClick = { /* TODO: location */ },
            onTrailingIconClick = { search(textFieldContents) },
            onTextFieldBlank = { error = false },
            onTextFieldChanged = { newString -> textFieldContents = newString },
            onKeyboardDone = { search(textFieldContents) })

        when (val cityState = cityStates.value) {
            is SearchState.Failure -> {
                error = true
                enabled = false
                textFieldContents = stringResource(R.string.remote_error)
            }
            is SearchState.CitiesResult -> {
                enabled = true
                ShowCities(onCitySelected, cities = cityState.results)
            }
            is SearchState.ZipResult -> {
                enabled = true
                ShowCities(onCitySelected, cities = listOf(cityState.results))
            }
            is SearchState.Loading -> {
                enabled = false
                FullScreenCenteredSpinner(cityState.isLoading)
            }
            SearchState.InvalidString -> error = true
            SearchState.Initial -> textFieldContents = hint
        }
    }
}

@Composable
fun ShowCities(onCitySelected: (City) -> Unit, cities: List<City>) {
    var expanded by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.testTag(TestTags.CITY_RESULTS_LIST),
        contentPadding = PaddingValues(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (expanded) {
            items(cities.size) { index ->
                val thisCity = cities[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .testTag(TestTags.CITY_RESULT)
                        .clickable {
                            onCitySelected(thisCity)
                            expanded = false
                        }, elevation = 4.dp
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = makeCityText(thisCity)
                    )
                }
            }
        }
    }
}

private fun makeCityText(thisCity: City) =
    listOfNotNull(thisCity.name, thisCity.state, thisCity.country).joinToString(", ")