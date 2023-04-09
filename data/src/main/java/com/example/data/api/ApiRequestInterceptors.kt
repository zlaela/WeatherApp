package com.example.data.api

import com.example.data.BuildConfig
import okhttp3.Interceptor

// Adds api key to each request
val apiKeyInterceptor = Interceptor { chain ->
    val url = chain.request()
        .url
        .newBuilder()
        .addQueryParameter("appid", BuildConfig.API_KEY) //TODO: replace with your own key in data module's build.gradle
        .build()
    val request = chain.request()
        .newBuilder()
        .url(url)
        .build()
    return@Interceptor chain.proceed(request)
}
