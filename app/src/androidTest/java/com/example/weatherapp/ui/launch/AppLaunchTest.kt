package com.example.weatherapp.ui.launch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.weatherapp.MainActivity
import com.example.weatherapp.ui.TestTags
import org.junit.Rule
import org.junit.Test

class AppLaunchTest {
    @get:Rule
    val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule()

    @Test
    fun showsSplashScreen() {
        splashScreenDisplayed()
        navigatesToMainScreen()
    }

    private fun splashScreenDisplayed() {
        rule.onNode(
            matcher = hasTestTag(testTag = TestTags.SPLASH)
        )
            .assertIsDisplayed()
    }

    private fun navigatesToMainScreen() {
        rule.mainClock.advanceTimeBy(2000L)
        rule.onNode(
            matcher = hasTestTag(testTag = TestTags.MAIN)
        )
            .assertIsDisplayed()
    }
}
