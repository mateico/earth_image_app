package com.example.earthimagesapp.presentation.day_listing

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.earthimagesapp.presentation.Screen
import com.example.earthimagesapplication.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DayListingScreen(
    viewModel: DayListingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val uiState: DayScreeState by viewModel.dayScreeState.collectAsState()
    val errorMessage = stringResource(id = R.string.error_text)
    val okText = stringResource(id = R.string.ok_button_text)

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    if (uiState.isError) {
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = okText
            )
            viewModel.onErrorConsumed()
        }
    }

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
                    onRefresh = { viewModel.onRefresh() }
                ) {
                    DayList(uiState.days, navController)

                }
            }


        )

    }
}

@Composable
fun DayList(uiState: DayListState, navController: NavController) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()

    ) {
        when (uiState) {
            DayListState.Error -> {
                homeSectionErrorText(R.string.section_error_days)
            }

            DayListState.Loading -> {
                item {
                    LoadingIndicator()
                }
            }

            is DayListState.Success -> {
                items(uiState.days.size) { i ->
                    val day = uiState.days[i]
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
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(color = Color.LightGray)
    }
}

fun LazyListScope.homeSectionErrorText(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    item {
        ErrorText(
            title = title,
            modifier = modifier
        )
    }
}

@Composable
fun ErrorText(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = title),
        modifier = modifier.padding(vertical = 24.dp)
    )
}


