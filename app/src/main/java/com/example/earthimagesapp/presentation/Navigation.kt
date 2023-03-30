package com.example.earthimagesapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.earthimagesapp.presentation.day_listing.DayListingScreen
import com.example.earthimagesapp.presentation.photo_listing.ImageListingScreen
import com.example.earthimagesapp.presentation.photo_screen.PhotoScreen
import com.example.earthimagesapp.presentation.splash_screen.SplashScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
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
            it.arguments?.getString("day")
                ?.let { ImageListingScreen(navController = navController) }
        }
        composable(
            route = Screen.PhotoDetailScreen.route + "/{identifier}",
            arguments = listOf(
                navArgument("identifier") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            it.arguments?.getString("identifier")
                ?.let { PhotoScreen(navController = navController) }
        }
    }
}


