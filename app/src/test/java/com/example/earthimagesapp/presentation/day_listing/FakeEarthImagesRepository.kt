package com.example.earthimagesapp.presentation.day_listing

import com.example.earthimagesapp.domain.DayRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDayRepository : DayRepository {

    private val days = mutableListOf<Day>()
    override fun getDaysFromLocal(): Flow<List<Day>> {
        return flow { emit(days)}
    }

    override suspend fun getDaysFromRemote() {
        TODO("Not yet implemented")
    }

    override suspend fun insertDays(days: List<Day>) {
        this.days.addAll(days)
    }
}