package com.example.earthimagesapp.presentation

sealed class Screen(var route: String) {
    object DayListingScreen : Screen("day_listing_screen")
    object PhotoListingScreen : Screen("photo_listing_screen")
    object PhotoDetailScreen : Screen("photo_detail_screen")
    object SplashScreen : Screen("splash_screen")
}
