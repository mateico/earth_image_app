package com.example.earthimagesapp.presentation.photo_listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.earthimagesapp.domain.model.ImageData
import com.example.earthimagesapp.util.DateUtils

@Composable
fun ImageItem(image: ImageData,
            modifier: Modifier = Modifier,
            onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()) {
           /* AsyncImage(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://epic.gsfc.nasa.gov/archive/enhanced/${DateUtils.formatDateToGetImage(image.date)}/png/epic_RGB_${image.identifier}.png")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(com.example.earthimagesapplication.R.drawable.placeholder),
                contentDescription = "This is the description",
                contentScale = ContentScale.Crop

            )*/

            AsyncImage(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://epic.gsfc.nasa.gov/archive/enhanced/${DateUtils.formatDateToGetImage(image.date)}/png/epic_RGB_${image.identifier}.png")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(com.example.earthimagesapplication.R.drawable.placeholder),
                contentDescription = "This is the description",
                contentScale = ContentScale.Crop

            )
            Text(text = image.date)
            Text(text = image.caption)
        }
    }
}