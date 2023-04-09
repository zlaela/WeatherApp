package com.example.weatherapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class SpyUiController : LifecycleOwner {
    private lateinit var lifecycleRegistry: LifecycleRegistry

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry


    open fun onCreate() {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
}