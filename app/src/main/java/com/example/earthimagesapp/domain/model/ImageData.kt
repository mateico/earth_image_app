package com.example.earthimagesapp.domain.model

data class ImageData(
    val caption: String,
    val date: String,
    val identifier: String,
    val image: String,
    val version: String?
)
