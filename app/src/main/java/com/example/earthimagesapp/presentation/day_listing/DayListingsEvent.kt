package com.example.earthimagesapp.presentation.day_listing

sealed class DayListingsEvent {
    object Refresh: DayListingsEvent()
    object CloseErrorMessage: DayListingsEvent()
}
