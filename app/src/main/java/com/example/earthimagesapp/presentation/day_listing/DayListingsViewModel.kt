package com.example.earthimagesapp.presentation.day_listing

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import androidx.work.*
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.util.*
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

    //val workInfo = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    private var imageUri: Uri? = null
    private val listUrls: MutableList<String> = mutableListOf()
    private var _listUrlsDownload = MutableLiveData<List<String>>()
    val listUrlsDownload: LiveData<List<String>>
        get() = _listUrlsDownload

    val mutableListWorkRequest: MutableList<WorkRequest> = mutableListOf()

    init {
        getDayListings()
    }

    fun down() {
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
    }

    fun onEvent(event: DayListingsEvent) {
        when (event) {
            is DayListingsEvent.Refresh -> {
                Timber.d("refreshing list")
                //getDayListings()
                down()
            }

            is DayListingsEvent.CloseErrorMessage -> {
                Timber.d("closing snack bar")
                state = state.copy(errorMessage = null)
            }
        }
    }


    private fun createInputData(): Data {
        val builder = Data.Builder()
        imageUri?.let { builder.putString(KEY_IMAGE_URI, it.toString()) }
        return builder.build()
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


}