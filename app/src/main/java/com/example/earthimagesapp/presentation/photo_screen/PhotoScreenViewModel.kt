package com.example.earthimagesapp.presentation.photo_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.earthimagesapp.domain.DayDataRepository
import com.example.earthimagesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: DayDataRepository
) : ViewModel() {

    var state by mutableStateOf(PhotoScreenState())

    init {
        getPhoto()
    }

    fun onEvent(event: PhotoScreenEvents) {
        when (event) {
            is PhotoScreenEvents.ShowMetadata -> {
                state = state.copy(showMetadata = !state.showMetadata)
            }


        }
    }

    private fun getPhoto() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("identifier") ?: return@launch
            repository
                .getPhotoDataById(id)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { item ->
                                state = state.copy(
                                    photo = item
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> Unit
                    }
                }
        }
    }

}