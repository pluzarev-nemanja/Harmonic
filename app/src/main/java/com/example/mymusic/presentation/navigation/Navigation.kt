package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymusic.presentation.player.PlayerScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route){

        composable(route = Screen.PlayerScreen.route){
            PlayerScreen()
        }
    }
}