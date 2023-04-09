package com.example.data.api

import com.example.data.api.response.city.Cities
import com.example.data.api.response.city.CityItem
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {
    @GET("geo/1.0/zip")
    fun searchZipAsync(
        @Query("zip") zip: String
    ): Deferred<CityItem>

    @GET("geo/1.0/direct")
    fun searchCityAsync(
        @Query("q") city: String
    ): Deferred<Cities>
}
