package com.example.earthimagesapp.domain

import com.example.earthimagesapp.domain.model.Day
import kotlinx.coroutines.flow.Flow

interface DayRepository {

    fun getDaysFromLocal(): Flow<List<Day>>

    suspend fun getDaysFromRemote()

    suspend fun insertDays(days: List<Day>)

}