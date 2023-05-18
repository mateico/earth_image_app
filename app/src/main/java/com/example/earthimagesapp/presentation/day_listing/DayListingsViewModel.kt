package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.domain.DayRepository
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.util.Result
import com.example.earthimagesapp.util.WhileUiSubscribed
import com.example.earthimagesapp.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DayScreeState(
    val days: DayListState,
    val isRefreshing: Boolean,
    val isError: Boolean
)

@Immutable
sealed interface DayListState {
    data class Success(val days: List<Day>) : DayListState
    object Error : DayListState
    object Loading : DayListState
}

@HiltViewModel
class DayListingsViewModel @Inject constructor(
    private val dayRepository: DayRepository,
    private val dayDataRepository: DayDataRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }

    private val days: Flow<Result<List<Day>>> = dayRepository.getDaysFromLocal().asResult()
    private val isRefreshing = MutableStateFlow(false)
    private val isError = MutableStateFlow(false)


    // combining the streams of days, isRefreshing and isError
    val dayScreeState: StateFlow<DayScreeState> = combine(
        days,
        isRefreshing,
        isError
    ) { daysResult, isRefreshing, isError ->

        val dayListState: DayListState = when (daysResult) {
            is Result.Success -> DayListState.Success(daysResult.data)
            is Result.Loading -> DayListState.Loading
            is Result.Error -> DayListState.Error
        }

        DayScreeState(
            dayListState,
            isRefreshing,
            isError
        )

        // to convert the combined data flow to stateflow I use stateIn
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed, // only one collector and wait 5 sec to restart after there are no connections
        initialValue = DayScreeState(
            DayListState.Loading,
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
                    isRefreshing.emit(false)
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
}