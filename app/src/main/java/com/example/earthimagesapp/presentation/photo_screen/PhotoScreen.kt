package com.example.earthimagesapp.presentation.photo_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.earthimagesapp.util.DateUtils
import com.example.earthimagesapp.util.IMAGE_PATH_START
import com.example.earthimagesapp.util.IMAGE_TYPE
import com.example.earthimagesapplication.R
import java.lang.Math.*
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PhotoScreen(
    viewModel: PhotoScreenViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state

    Scaffold(content = { innerPadding ->

        Column {


            TopAppBar(
                title = {
                    Text(stringResource(R.string.PhotoScreenTitle))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }, actions = {
                    IconButton(onClick = { viewModel.onEvent(PhotoScreenEvents.ShowMetadata) }) {
                        Icon(Icons.Filled.Info, null)
                    }

                })

            Box(modifier = Modifier.fillMaxSize()) {
                val angle by remember { mutableStateOf(0f) }
                var zoom by remember { mutableStateOf(1f) }
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }

                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp.value
                val screenHeight = configuration.screenHeightDp.dp.value

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            "https://epic.gsfc.nasa.gov/archive/enhanced/${
                                state.photo?.let {
                                    DateUtils.formatDateToGetImage(
                                        it.date
                                    )
                                }
                            }/png/epic_RGB_${state.photo?.identifier}.png"
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = "contentDescription",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .graphicsLayer(
                            scaleX = zoom,
                            scaleY = zoom,
                            rotationZ = angle
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures(
                                onGesture = { _, pan, gestureZoom, _ ->
                                    zoom = (zoom * gestureZoom).coerceIn(1F..4F)
                                    if (zoom > 1) {
                                        val x = (pan.x * zoom)
                                        val y = (pan.y * zoom)
                                        val angleRad = angle * PI / 180.0

                                        offsetX =
                                            (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                                -(screenWidth * zoom)..(screenWidth * zoom)
                                            )
                                        offsetY =
                                            (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                                -(screenHeight * zoom)..(screenHeight * zoom)
                                            )
                                    } else {
                                        offsetX = 0F
                                        offsetY = 0F
                                    }
                                }
                            )
                        }
                        .fillMaxSize()
                )
            }


        }

        if (state.showMetadata) {
            Card(
                modifier = Modifier
                    .padding(vertical = 70.dp, horizontal = 8.dp)
                    .clickable { viewModel.onEvent(PhotoScreenEvents.ShowMetadata) },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Identifier: ${state.photo?.identifier}")
                    Text(text = "Caption: ${state.photo?.caption}")
                    Text(text = "Version: ${state.photo?.version}")
                    Text(text = "Date: ${state.photo?.date}")
                    Text(text = "Image: ${state.photo?.image}")
                    Text(text = "Latitude: ${state.photo?.centroIdCoordinates?.lat}")
                    Text(text = "Longitude: ${state.photo?.centroIdCoordinates?.lon}")
                }
            }
        }

    })
}

