package com.example.earthimagesapp.data.mapper

import com.example.earthimagesapp.data.local.DayEntity
import com.example.earthimagesapp.domain.model.Day

fun DayEntity.toDay(): Day {
    return Day(
        date = date,
        status = status
    )
}

fun Day.toDayEntity(): DayEntity {
    return DayEntity(
        date = date,
        status = status
    )
}