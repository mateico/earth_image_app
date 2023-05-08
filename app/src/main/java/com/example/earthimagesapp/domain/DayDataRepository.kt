package com.example.earthimagesapp.domain

import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface DayDataRepository {

    suspend fun getImageDataByDayFromRemote()

    suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>>

    suspend fun getListImagesToDownload(): Flow<Resource<List<String>>>

    suspend fun getPhotoDataById(id: String): Flow<Resource<ImageData>>

}