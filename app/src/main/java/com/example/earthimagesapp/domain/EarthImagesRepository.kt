package com.example.earthimagesapp.domain

import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface EarthImagesRepository {

    fun getDays(): Flow<List<Day>>


    suspend fun refreshDays()

    suspend fun getImageDataByDayFromRemote(): Flow<Resource<Int>>

    suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>>

    suspend fun getListImagesToDownload(): Flow<Resource<List<String>>>

    suspend fun getPhotoDataById(id: String): Flow<Resource<ImageData>>

    suspend fun insertDays(days: List<Day>)


}