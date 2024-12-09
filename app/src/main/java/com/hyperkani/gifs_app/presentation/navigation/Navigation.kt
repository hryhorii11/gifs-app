package com.hyperkani.gifs_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperkani.gifs_app.presentation.screens.gifDetail.GifsDetailScreen
import com.hyperkani.gifs_app.presentation.screens.gisfList.GifListScreen
import com.hyperkani.gifs_app.presentation.screens.gisfList.GifsViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: GifsViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screens.LIST_SCREEN.route,
    ) {


        composable(Screens.LIST_SCREEN.route) { GifListScreen(navController, viewModel) }
        composable(route = Screens.GIF_SCREEN.route) {
            GifsDetailScreen(
                navHostController = navController,
                viewModel
            )
        }
    }
}