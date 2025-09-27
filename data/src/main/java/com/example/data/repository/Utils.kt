package com.example.data.repository

import retrofit2.HttpException
import java.net.UnknownHostException

fun reasonFor(throwable: Throwable) = when (throwable) {
    is UnknownHostException ->  "Network error"
    is HttpException -> "HTTP error ${throwable.code()}"
    else -> "Network error"
}