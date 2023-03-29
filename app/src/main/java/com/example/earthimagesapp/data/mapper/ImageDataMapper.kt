package com.example.earthimagesapp.data.mapper

import androidx.room.PrimaryKey
import com.example.earthimagesapp.data.local.DayEntity
import com.example.earthimagesapp.data.local.ImageDataEntity
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData

fun ImageDataEntity.toImageData(): ImageData {
    return ImageData(
        caption = caption,
        date = date,
        identifier = identifier,
        image = image,
        version = version
    )
}

fun ImageData.toImageDataEntity(): ImageDataEntity {
    return ImageDataEntity(
        caption = caption,
        date = date,
        identifier = identifier,
        image = image,
        version = version
    )
}