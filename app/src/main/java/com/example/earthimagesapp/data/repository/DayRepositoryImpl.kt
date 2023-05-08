package com.example.earthimagesapp.data.repository

import com.example.earthimagesapp.data.local.EarthImagesDatabase
import com.example.earthimagesapp.data.mapper.toDay
import com.example.earthimagesapp.data.mapper.toDayEntity
import com.example.earthimagesapp.data.remote.EarthImagesApi
import com.example.earthimagesapp.data.remote.dto.DayDto
import com.example.earthimagesapp.domain.DayRepository
import com.example.earthimagesapp.domain.model.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DayRepositoryImpl @Inject constructor(
    private val db: EarthImagesDatabase,
    private val api: EarthImagesApi
): DayRepository {

    private val dayDao = db.dayDao

    override fun getDaysFromLocal(): Flow<List<Day>> =
        dayDao.getDaysStream().map { entityDays ->
            entityDays.map { it.toDay() }
        }.onEach {
            if (it.isEmpty()) getDaysFromRemote()
        }

    override suspend fun getDaysFromRemote() {
        api.getDays()
            .also { dayDtos ->
                dayDao.insertOrIgnoreDays(days = dayDtos.map(DayDto::toDayEntity))
            }
    }

    override suspend fun insertDays(days: List<Day>) {
        dayDao.insertOrIgnoreDays(
            days.map { it.toDayEntity() }
        )
    }


}