package com.example.earthimagesapp.util

const val SPLASH_DELAY_TIME_MILLIS: Long = 2000L
const val KEY_IMAGE_URL = "IMAGE_URL"
const val KEY_IMAGE_PATH = "IMAGE_PATH"
const val TAG_OUTPUT = "OUTPUT"

const val DELAY_TIME_MILLIS: Long = 8000

@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notification"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

const val URL_SEPARATOR_CHARACTER = "/"
const val IMAGE_PATH_START = "/data/data/com.example.earthimagesapp/files/epic_RGB_"
const val IMAGE_TYPE = ".png"
const val IMAGE_URI_START = "https://epic.gsfc.nasa.gov/archive/enhanced/"
const val IMAGE_URI_MIDDLE = "/png/epic_RGB_"
