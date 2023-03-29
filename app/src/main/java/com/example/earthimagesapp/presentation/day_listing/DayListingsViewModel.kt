package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DayListingsViewModel @Inject constructor(
    private val repository: EarthImagesRepository
) : ViewModel() {

    var state by mutableStateOf(DayListingsState())

    init {
        //getDayListings()
        getImageByDateListing()
    }

    fun onEvent(event: DayListingsEvent) {
        when (event) {
            is DayListingsEvent.Refresh -> {
                Timber.d("refreshing list")
                getDayListings()
            }

            is DayListingsEvent.CloseErrorMessage -> {
                Timber.d("closing snack bar")
                state = state.copy(errorMessage = null)
            }
        }
    }

    fun getDayListings() {
        Timber.d("getting day")
        viewModelScope.launch {
            repository
                .getDays()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(
                                    days = listings
                                )
                            }
                        }

                        is Error -> {
                            Timber.e("Error loading list")
                            state = state.copy(errorMessage = result.message)
                        }

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        else -> {
                            state = state.copy(errorMessage = "Error getting the list of days")
                        }
                    }
                }
        }
    }

    fun getImageByDateListing() {
        Timber.d("getting images")
        viewModelScope.launch {
            repository
                .getImageByDay(false)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                Timber.d(listings.toString())
                            }
                        }

                        is Error -> {
                            Timber.e("Error loading list")
                            state = state.copy(errorMessage = result.message)
                        }

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        else -> {
                            state = state.copy(errorMessage = result.message)
                        }
                    }
                }
        }
    }

}