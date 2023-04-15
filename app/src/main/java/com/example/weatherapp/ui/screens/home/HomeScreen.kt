package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.ui.TestTags

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.testTag(TestTags.MAIN),
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent, elevation = 0.dp
            ) {
                Text(text = "Weather App")
            }
        },
    ) { padding ->
        MainContent(padding, navController = navController)
    }
}

@Composable
fun MainContent(
    paddingValues: PaddingValues,
    navController: NavController,
) {

}