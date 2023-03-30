package com.example.earthimagesapp.presentation.photo_listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.earthimagesapp.presentation.Screen
import com.example.earthimagesapplication.R
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Destination
fun ImageListingScreen(
    viewModel: ImageListingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state

    Column {
        TopAppBar(
            title = {
                Text("Images List (${state.day})")
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            }
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            content = {
                items(state.images.size) { i ->
                    val image = state.images[i]
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {

                        AsyncImage(
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                                .clickable { navController.navigate(Screen.PhotoDetailScreen.route + "/" + image.identifier) },

                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    "/data/data/com.example.earthimagesapp/files/epic_RGB_${image.identifier}.png"
                                )
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.placeholder),
                            contentDescription = "This is the description",
                            contentScale = ContentScale.Crop

                        )
                    }
                }
            })
    }
}
