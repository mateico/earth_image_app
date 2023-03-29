package com.example.earthimagesapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.earthimagesapp.presentation.day_listing.DayListingScreen
import com.example.earthimagesapp.presentation.photo_listing.ImageListingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.DayListingScreen.route) {
        composable(route = Screen.DayListingScreen.route) {
            DayListingScreen(navController = navController)
        }
        composable(
            route = Screen.PhotoListingScreen.route + "/{day}",
            arguments = listOf(
                navArgument("day") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            it.arguments?.getString("day")?.let { ImageListingScreen() }
        }
    }
}