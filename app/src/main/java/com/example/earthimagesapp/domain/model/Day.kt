package com.example.earthimagesapp.domain.model

import com.example.earthimagesapp.data.local.DayStatus

data class Day(
    val date: String,
    val status: DayStatus
)
