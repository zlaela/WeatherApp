package com.example.weatherapp.ui.search

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.data.domain.City
import com.example.weatherapp.MainActivity
import org.junit.Rule
import org.junit.Test

class CitySearchTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val badCity = ">Not_a_city<"
    private val searchCity = "Chicago"
    private val fetchedCities = listOf(
        City(name="Chicago", lat=-33.71745, lon=18.9963167, country="ZA", state="Western Cape"),
        City(name="Chicago", lat=-18.9535788, lon=29.7953081, country="ZW", state="Midlands Province"),
        City(name="Chicago", lat=4.9324371, lon=-52.331324, country="FR", state="French Guiana"),
        City(name="Chicago", lat=18.4172472, lon=-68.9756563, country="DO", state="La Romana"),
        City(name="Chicago", lat=41.8755616, lon=-87.6244212, country="US", state="Illinois")
    )

    @Test
    fun searchingCityShowsResults() {
        launchMainActivity(rule) {
            delayUntilSplashFinishes()
            typeCityIntoSearchBar(searchCity)
            clickSearchIcon()
            waitForExpectedResultsSize(fetchedCities.size)
        } verify {
            citiesDisplayed(fetchedCities)
        }
    }

    @Test
    fun selectingCityHidesResults() {
        launchMainActivity(rule) {
            delayUntilSplashFinishes()
            typeCityIntoSearchBar(searchCity)
            clickSearchIcon()
            waitForExpectedResultsSize(fetchedCities.size)
            selectFirstCity(fetchedCities.first())
        } verify {
            searchResultsAreHidden(fetchedCities.size)
        }
    }

    @Test
    fun invalidCityShowsError() {
        launchMainActivity(rule) {
            delayUntilSplashFinishes()
            typeCityIntoSearchBar(badCity)
            clickSearchIcon()
        } verify {
            invalidTextIsDisplayed()
        }
    }
}