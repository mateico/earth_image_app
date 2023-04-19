package com.example.earthimagesapp.presentation.day_listing

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.util.*
import com.example.earthimagesapp.workers.WordManagerDownloadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class DayListingState(
    val days: DaysUiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)
@Immutable
sealed interface DaysUiState {
    data class Success(val days: List<Day>): DaysUiState
    object Error : DaysUiState
    object Loading : DaysUiState
}

@HiltViewModel
class DayListingsViewModel @Inject constructor(
    private val repository: EarthImagesRepository
) : ViewModel() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }

    private val days: Flow<Result<List<Day>>> =
        repository.getDays().asResult()

    private val isRefreshing = MutableStateFlow(false)

    private val isError = MutableStateFlow(false)

    //var state by mutableStateOf(DayListingsState())
    //private val mutableListWorkRequest: MutableList<WorkRequest> = mutableListOf()

    val uiState: StateFlow<DayListingState> = combine(
        days,
        isRefreshing,
        isError
    ) {daysResult, isRefreshing,isError ->

        val days: DaysUiState = when (daysResult) {
            is Result.Success -> DaysUiState.Success(daysResult.data)
            is Result.Loading -> DaysUiState.Loading
            is Result.Error -> DaysUiState.Error
        }

        DayListingState(
            days,
            isRefreshing,
            isError
        )

    } .stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = DayListingState(
            DaysUiState.Loading,
            isRefreshing = false,
            isError = false
        )
    )

    fun onRefresh() {
        viewModelScope.launch(exceptionHandler) {
            with(repository) {
                val refreshDaysDeferred = async { refreshDays() }
                isRefreshing.emit(true)
                try {
                    awaitAll(
                        refreshDaysDeferred
                    )
                } finally {
                    isRefreshing.emit(false)
                }
            }
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            isError.emit(false)
        }
    }

    /*init {
        //getData()
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
    }*/
}