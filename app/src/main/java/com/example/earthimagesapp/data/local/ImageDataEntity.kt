package com.example.earthimagesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.earthimagesapp.domain.model.CentroIdCoordinates

@Entity
data class ImageDataEntity(
    val caption: String,
    val date: String,
    @PrimaryKey val identifier: String,
    val image: String,
    val version: String?,
    val centroIdCoordinates : CentroIdCoordinates?
)
