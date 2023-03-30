package com.example.earthimagesapp.presentation.day_listing

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.util.KEY_IMAGE_URL
import com.example.earthimagesapp.util.Resource
import com.example.earthimagesapp.workers.WordManagerDownloadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DayListingsViewModel @Inject constructor(
    private val repository: EarthImagesRepository,
    application: Application,
    private val workManager: WorkManager
) : ViewModel() {

    var state by mutableStateOf(DayListingsState())
    private val mutableListWorkRequest: MutableList<WorkRequest> = mutableListOf()

    init {
        getData()
    }

    private fun downloadImages() {
        viewModelScope.launch {
            repository.getListImagesToDownload().collect { result ->
                result.data?.forEach { url ->
                    mutableListWorkRequest.add(
                        OneTimeWorkRequestBuilder<WordManagerDownloadImage>()
                            .setInputData(workDataOf(KEY_IMAGE_URL to url))
                            .setConstraints(
                                Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                            )
                            .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS
                            ).build()
                    )
                }
            }
            WorkManager.getInstance().enqueue(mutableListWorkRequest)

        }

        Handler(Looper.getMainLooper()).postDelayed({
            state = state.copy(isLoading = false)
        }, 3000)
    }

    fun onEvent(event: DayListingsEvent) {
        when (event) {
            is DayListingsEvent.Refresh -> {
                // do refresh
            }
            is DayListingsEvent.CloseErrorMessage -> {
                state = state.copy(errorMessage = null)
            }
        }
    }

    private fun getData() {
        getDayListings()
    }

    private fun getDayListings() {
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
                            getImagesData()
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
                            state = state.copy(isLoading = false)
                        }
                    }
                }
        }
    }

    private fun getImagesData() {
        viewModelScope.launch {
            repository
                .getImageDataByDayFromRemote()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { index ->
                                if (index == 10) {
                                    downloadImages()
                                }
                            }
                        }

                        is Error -> {
                            Timber.e("Error loading list")
                            state = state.copy(errorMessage = result.message)
                        }

                        is Resource.Loading -> {
                            //state = state.copy(isLoading = result.isLoading)
                        }
                        else -> {
                            state = state.copy(errorMessage = "Error getting the list of days")
                        }
                    }
                }
        }
    }
}