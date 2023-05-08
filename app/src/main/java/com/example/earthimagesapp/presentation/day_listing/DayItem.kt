package com.example.earthimagesapp.presentation.day_listing

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.earthimagesapp.data.local.DayStatus
import com.example.earthimagesapp.domain.model.Day
import com.example.earthimagesapp.util.DateUtils

@Composable
fun DayItem(
    day: Day,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(text = DateUtils.dateFormatForDayList(day.date))
            when(day.status){
              DayStatus.EMPTY -> Dot(androidx.compose.ui.graphics.Color.Red)
              DayStatus.LOADING -> Dot(androidx.compose.ui.graphics.Color.Yellow)
              DayStatus.WITH_DATA -> Dot(androidx.compose.ui.graphics.Color.Green)
            }
        }
    }
}

@Composable
fun Dot(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color, shape = CircleShape)
    )
}