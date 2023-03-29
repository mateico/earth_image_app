package com.example.earthimagesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.earthimagesapp.data.remote.dto.ImageDataDto

@Database(
    entities = [
        DayEntity::class,
        ImageDataEntity::class],
    version = 4
)

abstract class EarthImagesDatabase: RoomDatabase() {
    abstract val dayDao: DayDao
    abstract val imageDataDao: ImageDataDao
}