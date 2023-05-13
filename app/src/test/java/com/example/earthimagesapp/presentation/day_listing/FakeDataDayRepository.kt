package com.example.earthimagesapp.presentation.day_listing

import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow

class FakeDataDayRepository: DayDataRepository {
    override suspend fun getImageDataByDayFromRemote() {
        TODO("Not yet implemented")
    }

    override suspend fun getImageDataByDayFromLocal(day: String): Flow<Resource<List<ImageData>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPhotoDataById(id: String): Flow<Resource<ImageData>> {
        TODO("Not yet implemented")
    }
}