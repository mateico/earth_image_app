package com.example.earthimagesapp.data.mapper

import com.example.earthimagesapp.data.local.ImageDataEntity
import com.example.earthimagesapp.domain.model.ImageData

fun ImageDataEntity.toImageData(): ImageData {
    return ImageData(
        caption = caption,
        date = date,
        identifier = identifier,
        image = image,
        version = version,
        centroIdCoordinates = centroIdCoordinates
    )
}

fun ImageData.toImageDataEntity(): ImageDataEntity {
    return ImageDataEntity(
        caption = caption,
        date = date,
        identifier = identifier,
        image = image,
        version = version,
        centroIdCoordinates = centroIdCoordinates
    )
}