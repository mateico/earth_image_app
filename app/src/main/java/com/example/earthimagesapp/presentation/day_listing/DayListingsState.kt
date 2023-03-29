package com.example.earthimagesapp.presentation.day_listing

import com.example.earthimagesapp.domain.model.Day

data class DayListingsState(
    val days: List<Day> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)
