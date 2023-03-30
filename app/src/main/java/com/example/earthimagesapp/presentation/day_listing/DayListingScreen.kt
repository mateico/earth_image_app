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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer

import androidx.navigation.NavController
import androidx.work.*
import androidx.work.R
import com.example.earthimagesapp.presentation.Screen
import com.example.earthimagesapp.util.KEY_IMAGE_URL
import com.example.earthimagesapp.workers.WordManagerDownloadImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import java.util.concurrent.TimeUnit

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Destination
fun DayListingScreen(
    viewModel: DayListingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    /*val workInfo = viewModel.workInfo.observeAsState().value
    val downloadInfo = remember(workInfo) {
        workInfo?.find { it.tags.contains(TAG_OUTPUT) }
    }
    val mutableListWorkRequest: MutableList<WorkRequest> = mutableListOf()

    viewModel.listUrlsDownload.observe(LocalLifecycleOwner.current, Observer { listUrls ->
        //toImage.text = listUrls.count().toString()
        listUrls.forEach { url ->
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
    })
    WorkManager.getInstance().enqueue(mutableListWorkRequest)
    mutableListWorkRequest.forEach { workRequest ->
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.id)
            .observe(LocalLifecycleOwner.current, Observer { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    *//* totalQuantityOfImagesProcessed += 1
                     fromImage.text = totalQuantityOfImagesProcessed.toString()
                     val messageImage = if (workInfo.state == WorkInfo.State.SUCCEEDED)
                         workInfo.outputData.getString(KEY_IMAGE_PATH)
                     else getString(R.string.error_dowload_image)
                     message.text = messageImage*//*
                }
            })
    }*/



    Column {
        TopAppBar(
            title = {
                Text("Days List")
            },
        )
/*
        when (workInfo?.) {
            WorkInfo.State.CANCELLED -> Text(text = "Download cancelled")
            WorkInfo.State.ENQUEUED -> Text(text = "Download enqueued")
            WorkInfo.State.RUNNING -> Text(text = "Downloading")
            WorkInfo.State.SUCCEEDED -> Text(text = "Download succeeded")
            WorkInfo.State.FAILED -> Text(text = "Download failed")
            WorkInfo.State.BLOCKED -> Text(text = "Download blocked")
            else -> Text(text = "Something wrong happened")
        }*/
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


