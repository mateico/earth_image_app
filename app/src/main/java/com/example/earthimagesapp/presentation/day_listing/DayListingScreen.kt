package com.example.earthimagesapp.presentation.day_listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.*
import com.example.earthimagesapp.presentation.Screen
import com.example.earthimagesapplication.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
                Text(stringResource(R.string.DaysListScreenTitle))
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

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(80.dp, 80.dp)
                        )
                    }
                }
            }

        )
    }
}


