package com.example.earthimagesapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.earthimagesapp.presentation.day_listing.DayListingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.DayListingScreen.route) {
        composable(route = Screen.DayListingScreen.route) {
            DayListingScreen(navController = navController)
        }

    }
}