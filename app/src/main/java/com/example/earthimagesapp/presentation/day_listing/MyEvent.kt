package com.example.earthimagesapp.presentation.day_listing

sealed class MyEvent {
    object DownloadImage: MyEvent()
    object CancelWork: MyEvent()
}
