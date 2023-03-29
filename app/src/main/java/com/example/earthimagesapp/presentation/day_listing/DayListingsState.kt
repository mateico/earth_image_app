package com.example.earthimagesapp.presentation.day_listing

data class DayListingsState(
    //val products: List<ProductListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)
