package com.example.earthimagesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDayListing(DayEntity: List<DayEntity>)

    @Query("SELECT * FROM dayentity")
    suspend fun getDayListing(): List<DayEntity>


}