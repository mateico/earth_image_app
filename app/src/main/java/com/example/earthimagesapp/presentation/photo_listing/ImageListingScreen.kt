package com.example.earthimagesapp.presentation.photo_listing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.earthimagesapp.presentation.day_listing.DayItem
import com.example.earthimagesapp.presentation.day_listing.DayListingsEvent
import com.example.earthimagesapp.presentation.day_listing.DayListingsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Destination
fun ImageListingScreen(
    viewModel: ImageListingsViewModel = hiltViewModel(),
) {

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    Scaffold(
        content = { innerPadding ->
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { Unit }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.images.size) { i ->
                        val image = state.images[i]
                        ImageItem(
                            image = image,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)

                        ) {
                            //navController.navigate(Screen.PhotoListingScreen.route + "/" + day.date)
                        }
                    }
                }
            }
          /*  if (state.errorMessage != null) {
                Snackbar(
                    action = {
                        Button(onClick = {
                            viewModel.onEvent(DayListingsEvent.CloseErrorMessage)
                        }) {
                            Text("Close")
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) { Text(text = state.errorMessage) }
            }*/
        }
    )
}
