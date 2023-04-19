package com.example.earthimagesapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Query("SELECT * FROM dayentity")
    fun getDaysStream(): Flow<List<DayEntity>>

    /**
     * Inserts [days] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreDays(days: List<DayEntity>)


}