package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Query("SELECT * FROM cities WHERE name = :city")
    fun getCities(city: String): List<CityEntity>

    @Query("SELECT * FROM cities WHERE city_id = :id")
    fun getCity(id: Int): CityEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: CityEntity)
}