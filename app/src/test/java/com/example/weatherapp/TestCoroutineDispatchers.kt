package com.example.weatherapp

import com.example.weatherapp.viewmodel.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineDispatchers : CoroutineDispatchers {
    override val ui: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val background: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}