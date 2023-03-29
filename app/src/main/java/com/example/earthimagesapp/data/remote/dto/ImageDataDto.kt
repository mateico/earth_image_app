package com.example.earthimagesapp.data.remote.dto

import com.example.earthimagesapp.data.local.DayEntity
import com.example.earthimagesapp.data.local.ImageDataEntity

data class ImageDataDto(
    val caption: String,
    val date: String,
    val identifier: String,
    val image: String,
    val version: String
) {
    fun toImageDataEntity(): ImageDataEntity {
        return ImageDataEntity(
            caption = caption,
            date = date,
            identifier = identifier,
            image = image,
            version = version
        )
    }
}