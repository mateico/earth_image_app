package com.example.earthimagesapp.presentation.day_listing

import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEarthImagesRepository : EarthImagesRepository {

    private val days = mutableListOf<Day>()

    override suspend fun getDays(): Flow<Resource<List<Day>>> {
        return flow { emit(Resource.Success(days)) }
    }

    override suspend fun getImageDataByDayFromRemote(): Flow<Resource<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListImagesToDownload(): Flow<Resource<List<String>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPhotoDataById(id: String): Flow<Resource<ImageData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertDays(days: List<Day>) {
        this.days.addAll(days)
    }
}