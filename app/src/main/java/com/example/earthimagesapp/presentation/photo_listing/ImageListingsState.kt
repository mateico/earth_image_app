package com.example.earthimagesapp.presentation.photo_listing

import com.example.earthimagesapp.domain.model.ImageData

data class ImageListingsState(
    val images: List<ImageData> = emptyList(),
    val isRefreshing: Boolean = false,
    val day: String? = null
)
