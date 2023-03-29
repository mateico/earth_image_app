package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DayListingsViewModel @Inject constructor(
) : ViewModel() {

    var state by mutableStateOf(DayListingsState())

    init {
        getDayListings()
    }



    fun getDayListings() {

    }

}