package com.example.data.dao

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.api.response.city.LocalNames
import com.squareup.moshi.Json

@Entity(tableName = "cities")
data class CityEntity (
    @Json(name = "name")
    val name: String,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "local_names")
    @Embedded
    val localNames: LocalNames? = null,
    @PrimaryKey(autoGenerate = false) val city_id: Int = -1,
)