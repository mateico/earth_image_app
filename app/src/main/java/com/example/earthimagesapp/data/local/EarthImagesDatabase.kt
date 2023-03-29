package com.example.earthimagesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DayEntity::class],
    version = 1
)

abstract class EarthImagesDatabase: RoomDatabase() {
    abstract val dayDao: DayDao
}