package com.example.earthimagesapp.util

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    fun formatDateToGetImage(date: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateValue: Date = input.parse(date)
        val output = SimpleDateFormat("yyyy/MM/dd")
        return output.format(dateValue)
    }

}