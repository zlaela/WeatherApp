package com.example.weatherapp.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R
import com.example.weatherapp.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar() {
    TopAppBar(modifier = Modifier.testTag(TestTags.APP_BAR),
        title = { Text(text = stringResource(id = R.string.app_name)) })
}