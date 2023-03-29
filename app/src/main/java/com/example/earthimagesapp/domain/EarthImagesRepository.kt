package com.example.earthimagesapp.domain

import com.example.earthimagesapp.data.remote.dto.ImageDataDto
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface EarthImagesRepository {

    suspend fun getDays(): Flow<Resource<List<Day>>>

    suspend fun getImageByDay(fetchFromLocal: Boolean): Flow<Resource<List<ImageData>>>

    suspend fun insertDays(products: List<Day>)

}