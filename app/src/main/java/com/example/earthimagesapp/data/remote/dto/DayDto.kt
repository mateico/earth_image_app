package com.example.earthimagesapp.data.remote.dto

import com.example.earthimagesapp.data.local.DayEntity
import com.example.earthimagesapp.data.local.DayStatus

data class DayDto(
    val date: String
) {
    fun toDayEntity(): DayEntity {
        return DayEntity(
            date = date,
            status = DayStatus.EMPTY
        )
    }
}