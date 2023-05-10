package com.example.earthimagesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayEntity(
    @PrimaryKey val date: String,
    val status: DayStatus
)
