package com.example.earthimagesapp.presentation.photo_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.earthimagesapp.util.DateUtils
import com.example.earthimagesapplication.R
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun PhotoScreen(viewModel: PhotoScreenViewModel = hiltViewModel()) {

    val state = viewModel.state

    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {


        state.photo?.caption?.let { Text(text = it) }

    }

}