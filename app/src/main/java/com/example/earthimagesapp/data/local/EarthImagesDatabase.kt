package com.example.earthimagesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        DayEntity::class,
        ImageDataEntity::class],
    version = 14
)
@TypeConverters(Converters::class)

abstract class EarthImagesDatabase : RoomDatabase() {
    abstract val dayDao: DayDao
    abstract val imageDataDao: ImageDataDao
}