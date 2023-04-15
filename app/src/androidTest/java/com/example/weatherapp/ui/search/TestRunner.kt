package com.example.weatherapp.ui.search

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.data.domain.City
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags

fun launchMainActivity(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: TestRunner.() -> Unit,
): TestRunner {
    return TestRunner(rule).apply(block)
}

class TestRunner(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    private val searchBarHint = rule.activity.getString(R.string.hint_enter_location)

    fun delayUntilSplashFinishes() {
        rule.mainClock.advanceTimeBy(2000L)
    }

    fun typeCityIntoSearchBar(cityName: String) {
        rule.onNodeWithTag(TestTags.SEARCH_FIELD)
            .assertTextContains(searchBarHint)
            .performTextInput(cityName)
    }

    fun clickSearchIcon() {
        rule.onNodeWithTag(TestTags.SEARCH_ICON, true).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitForExpectedResultsSize(expectedCitiesCount: Int) {
        rule.waitUntilAtLeastOneExists(matcher = hasTestTag(TestTags.CITY_RESULTS_LIST))

        rule.waitUntilNodeCount(
            matcher = hasTestTag(TestTags.CITY_RESULT),
            count = expectedCitiesCount,
            timeoutMillis = 1000L
        )
    }

    fun selectFirstCity(city: City) {
        val cityText = "${city.name}:: ${city.state}, ${city.country} -- ${city.lat} ${city.lon}"
        rule.onNodeWithText(cityText)
            .assertIsDisplayed()
            .performClick()
            .performSemanticsAction(SemanticsActions.OnClick)
    }

    class CitiesSearchVerification(
        private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    ) {
        private val searchBarHintInvalid = rule.activity.getString(R.string.hint_invalid_location)

        fun citiesDisplayed(cities: List<City>) {
            rule.onAllNodesWithTag(TestTags.CITY_RESULT)
                .assertCountEquals(cities.size)

            cities.forEach { city ->
                rule.onNodeWithText("${city.name}:: ${city.state}, ${city.country} -- ${city.lat} ${city.lon}")
                    .assertIsDisplayed()
            }
        }

        fun searchResultsAreHidden(resultsSize: Int) {
            for(i in 0 until resultsSize) {
                rule.onNodeWithTag(TestTags.CITY_RESULTS_LIST)
                    .onChildAt(i)
                    .assertIsNotDisplayed()
            }
        }

        fun invalidTextIsDisplayed() {
            rule.onNodeWithTag(TestTags.SEARCH_FIELD)
                .assertTextContains(searchBarHintInvalid)
        }
    }

    infix fun verify(block: CitiesSearchVerification.() -> Unit): CitiesSearchVerification =
        CitiesSearchVerification(rule).apply(block)
}
