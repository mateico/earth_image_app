package com.example.earthimagesapp.util

const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

//Keys
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
const val OUTPUT_PATH = "image_output"
const val TAG_OUTPUT = "OUTPUT"

const val DELAY_TIME_MILLIS: Long = 8000

@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notification"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

const val KEY_IMAGE_URL = "IMAGE_URL"
const val KEY_IMAGE_PATH = "IMAGE_PATH"
const val URL_SEPARATOR_CHARACTER = "/"