package com.example.earthimagesapp.presentation.photo_listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.domain.model.ImageData

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
            Text(text = image.date)
            Text(text = image.caption)
        }
    }
}