package com.maral.happyplacesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maral.happyplacesapp.ui.screens.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Defines all navigation routes of the app.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {

        // Home screen with place list
        composable("home") {
            HomeScreen(navController)
        }

        // Screen to add a new place
        composable("add_place") {
            AddPlaceScreen(navController)
        }

        // Screen to show place details
        composable(
            route = "place/{placeId}",
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
            PlaceDetailScreen(navController, placeId)
        }

        // Screen to edit an existing place
        composable(
            route = "edit_place/{placeId}",
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
            EditPlaceScreen(navController, placeId)
        }

        // 🆕 New screen to show all places on a big map
        composable("map_all_places") {
            MapAllPlacesScreen()
        }
    }
}

