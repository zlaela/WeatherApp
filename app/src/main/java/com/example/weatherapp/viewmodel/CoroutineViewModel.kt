package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Common viewmodel that will handle the launch and cancellation of Jobs
 */
interface CoroutineDispatchers {
    val ui: CoroutineDispatcher
    val background: CoroutineDispatcher
}

abstract class CoroutineViewModel(
    private val dispatchers: CoroutineDispatchers
) : ViewModel(), CoroutineScope {

    //TODO: remove or fix
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    // Parent of all child coroutines originating from viewmodels that extend this
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.ui + job

    val exceptionContext: CoroutineContext
            get() = coroutineContext + coroutineExceptionHandler

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
