package com.example.earthimagesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DayEntity::class,
        ImageDataEntity::class],
    version = 7
)

abstract class EarthImagesDatabase : RoomDatabase() {
    abstract val dayDao: DayDao
    abstract val imageDataDao: ImageDataDao
}