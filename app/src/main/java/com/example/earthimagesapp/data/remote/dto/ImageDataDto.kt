package com.example.earthimagesapp.data.remote.dto

import com.example.earthimagesapp.data.local.ImageDataEntity
import com.example.earthimagesapp.domain.model.CentroIdCoordinates
import com.google.gson.annotations.SerializedName

data class ImageDataDto(
    val caption: String,
    val date: String,
    val identifier: String,
    val image: String,
    val version: String?,
    //@SerializedName("centroid_coordinates")
    val centroid_coordinates : CentroIdCoordinates
) {
    fun toImageDataEntity(): ImageDataEntity {
        return ImageDataEntity(
            caption = caption,
            date = date,
            identifier = identifier,
            image = image,
            version = version,
            centroIdCoordinates = centroid_coordinates
        )
    }
}