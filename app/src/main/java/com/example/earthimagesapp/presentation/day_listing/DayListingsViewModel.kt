package com.example.earthimagesapp.presentation.day_listing

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        /*for (line in 1..100) {
            listUrls.add("https://picsum.photos/300/300?image=$line")
        }
        _listUrlsDownload.value = listUrls
        down()*/

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


            /* ) .value?.forEach { url ->
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
             }*/
            WorkManager.getInstance().enqueue(mutableListWorkRequest)
            /*mutableListWorkRequest.forEach { workRequest ->
                WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.id)
                    .observe(LocalLifecycleOwner.current, Observer { workInfo ->
                        if (workInfo != null && workInfo.state.isFinished) {
                            *//* totalQuantityOfImagesProcessed += 1
                         fromImage.text = totalQuantityOfImagesProcessed.toString()
                         val messageImage = if (workInfo.state == WorkInfo.State.SUCCEEDED)
                             workInfo.outputData.getString(KEY_IMAGE_PATH)
                         else getString(R.string.error_dowload_image)
                         message.text = messageImage*//*
                    }
                })
        }*/
        }}
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

        fun onWorkEvent(e: MyEvent) {
            when (e) {
                MyEvent.DownloadImage -> saveImage()
                MyEvent.CancelWork -> cancelWork()
            }
        }

        private fun saveImage() {
            // set Constraints
            /* val constraints = Constraints.Builder()
                 .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                 .setRequiresStorageNotLow(true)
                 .setRequiresBatteryNotLow(true)
                 .build()

             val imageWorker = OneTimeWorkRequestBuilder<MyWorker>()
                 .setConstraints(constraints)
                 .setInputData(createInputData())
                 .addTag(TAG_OUTPUT)
                 .build()

             workManager.enqueueUniqueWork(
                 IMAGE_MANIPULATION_WORK_NAME,
                 ExistingWorkPolicy.REPLACE,
                 imageWorker
             )*/


        }

        private fun cancelWork() {
            workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
        }

        private fun getImageUri(context: Context): Uri {
            val resources = context.resources

            return Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .build()

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

        /* fun getImageByDateListing(day: String) {
             Timber.d("getting images")
             viewModelScope.launch {
                 repository
                     .getImageByDayFromRemote(day)
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
         }*/

    }