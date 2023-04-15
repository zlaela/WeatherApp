package com.example.weatherapp.ui.composable

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags

@Composable
fun AppTopAppBar() {
    TopAppBar(modifier = Modifier.testTag(TestTags.APP_BAR),
        title = { Text(text = stringResource(id = R.string.app_name)) })
}