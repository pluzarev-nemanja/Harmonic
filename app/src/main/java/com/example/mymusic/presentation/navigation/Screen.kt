package com.example.mymusic.presentation.navigation

sealed class Screen(val route:String){
    object SearchScreen: Screen("search_screen")
    object PlaylistDetailsScreen: Screen("playlist_details_screen")

    object AlbumDetailScreen : Screen("album_detail_screen")
    object HistoryScreen : Screen("history_screen")

    object FavoriteScreen : Screen("favorite_screen")
}
