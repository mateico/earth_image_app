package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import com.example.earthimagesapp.presentation.Screen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
fun DayListingScreen(
    viewModel: DayListingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    Column {
        TopAppBar(
            title = {
                Text("Days List")
            },
        )
        Scaffold(
            content = { innerPadding ->
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.onEvent(DayListingsEvent.Refresh) }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.days.size) { i ->
                            val day = state.days[i]
                            DayItem(
                                day = day,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)

                            ) {
                                navController.navigate(Screen.PhotoListingScreen.route + "/" + day.date)
                            }
                        }
                    }
                }
                if (state.errorMessage != null) {
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
                }
            }
        )
    }
}


