package com.example.weatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.screens.home.HomeScreen
import com.example.weatherapp.ui.screens.splash.AppSplashScreen

@Composable
fun AppNavigationController(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(
            route = Screens.Splash.route,
        ) { _ ->
            AppSplashScreen(navController = navController)
        }

        composable(
            route = Screens.Home.route
        ) { _ ->
            HomeScreen(navController = navController)
        }
    }
}
