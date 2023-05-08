package com.example.earthimagesapp.presentation.day_listing

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.*
import com.example.earthimagesapp.presentation.Screen
import com.example.earthimagesapp.util.TAG_OUTPUT
import com.example.earthimagesapplication.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import timber.log.Timber

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DayListingScreen(
    viewModel: DayListingsViewModel = hiltViewModel(),
    navController: NavController
) {

    //val workInfo = viewModel.workInfo.observeAsState().value
    /*val downloadInfo = remember(workInfo) {
        workInfo?.find { it.tags.contains(TAG_OUTPUT) }
    }*/

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState: DayListingState by viewModel.uiState.collectAsState()
    //val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.onRefresh() })
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
                /* when (downloadInfo?.state) {
                     WorkInfo.State.CANCELLED -> Text(text = "Download cancelled")
                     WorkInfo.State.ENQUEUED -> Text(text = "Download enqueued")
                     WorkInfo.State.RUNNING -> Text(text = "Downloading")
                     WorkInfo.State.SUCCEEDED -> Text(text = "Download succeeded")
                     WorkInfo.State.FAILED -> Text(text = "Download failed")
                     WorkInfo.State.BLOCKED -> Text(text = "Download blocked")
                     else -> Text(text = "Something wrong happened")
                 }*/


            },
            floatingActionButton = { FloatingActionButton(onClick = { viewModel.downloadImages() }) }


        )

    }
}

@Composable
fun DayList(uiState: DaysUiState, navController: NavController) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()

    ) {
        when (uiState) {
            DaysUiState.Error -> {
                homeSectionErrorText(R.string.section_error_days)
            }

            DaysUiState.Loading -> {
                item {
                    LoadingIndicator()
                }
            }

            is DaysUiState.Success -> {
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

@Composable
fun FloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
        }
    ) {
        Icon(Icons.Filled.Download, "")
    }
}

@Preview(showBackground = false)
@Composable
fun FloatingActionButtonPreview() {
    //FloatingActionButton()
}
