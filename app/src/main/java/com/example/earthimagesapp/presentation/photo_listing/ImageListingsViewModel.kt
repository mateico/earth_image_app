package com.example.earthimagesapp.presentation.photo_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.earthimagesapp.domain.EarthImagesRepository
import com.example.earthimagesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: EarthImagesRepository
) : ViewModel()  {

    var state by mutableStateOf(ImageListingsState())

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            val day = savedStateHandle.get<String>("day") ?: return@launch
            state = state.copy(day = day)
            repository
                .getImageDataByDayFromLocal(day)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { item ->
                                state = state.copy(
                                    images = item
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