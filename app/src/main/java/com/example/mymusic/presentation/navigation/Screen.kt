package com.example.mymusic.presentation.navigation

sealed class Screen(val route:String){
    object SearchScreen: Screen("search_screen")
}
