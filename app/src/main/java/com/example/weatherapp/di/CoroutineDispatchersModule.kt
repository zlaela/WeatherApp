package com.example.weatherapp.di

import com.example.weatherapp.viewmodel.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * This is an object because it doesn't store any state (aka just provides a value)
 * Could also be a class
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @Provides
    fun provideDispatchers(): CoroutineDispatchers =
        object : CoroutineDispatchers {
            override val ui: CoroutineDispatcher
                get() = Dispatchers.Main
            override val background: CoroutineDispatcher
                get() = Dispatchers.IO
        }
}