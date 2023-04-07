package com.example.weatherapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

@OptIn(ExperimentalCoroutinesApi::class)
class ExecutionExtension(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(extensionContext: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(extensionContext: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}