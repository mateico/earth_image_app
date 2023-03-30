package com.example.earthimagesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagesByDayListing(DayEntity: List<ImageDataEntity>)

    @Query(
        """
        SELECT * 
        FROM imagedataentity 
        WHERE date LIKE '%' || LOWER(:date) || '%'
        """
    )
    suspend fun getImagesByDayListing(date: String): List<ImageDataEntity>

    @Query("SELECT * FROM imagedataentity")
    suspend fun getImagesByDayListing(): List<ImageDataEntity>

    @Query("SELECT * FROM imagedataentity WHERE identifier = :id")
    suspend fun getImageById(id: String): ImageDataEntity
}