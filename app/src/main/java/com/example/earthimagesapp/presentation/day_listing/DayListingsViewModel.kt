package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.domain.DayRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.util.*
import com.example.earthimagesapp.workers.WordManagerDownloadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DayListingState(
    val days: DaysUiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)

@Immutable
sealed interface DaysUiState {
    data class Success(val days: List<Day>) : DaysUiState
    object Error : DaysUiState
    object Loading : DaysUiState
}

@HiltViewModel
class DayListingsViewModel @Inject constructor(
    private val dayRepository: DayRepository,
    private val dayDataRepository: DayDataRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }

    private val days: Flow<Result<List<Day>>> = dayRepository.getDaysFromLocal().asResult()
    private val isRefreshing = MutableStateFlow(false)
    private val isError = MutableStateFlow(false)

    private val mutableListWorkRequest: MutableList<WorkRequest> = mutableListOf()

    val uiState: StateFlow<DayListingState> = combine(
        days,
        isRefreshing,
        isError
    ) { daysResult, isRefreshing, isError ->

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

    }.stateIn(
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
            with(dayRepository) {
                isRefreshing.emit(true)
                try {
                    getDaysFromRemote()
                    dayDataRepository.getImageDataByDayFromRemote()
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

    fun downloadImages() {
        viewModelScope.launch {
            dayDataRepository.getListImagesToDownload().collect { result ->
                result.data?.forEach { url ->
                    mutableListWorkRequest.add(
                        OneTimeWorkRequestBuilder<WordManagerDownloadImage>()
                            .setInputData(workDataOf(KEY_IMAGE_URL to url))
                            .setConstraints(
                                Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()

                            )
                            .addTag(TAG_OUTPUT)
                            .build()
                    )
                }
            }
            workManager.enqueue(mutableListWorkRequest)
        }
    }
}