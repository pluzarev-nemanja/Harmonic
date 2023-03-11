package com.example.mymusic.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon : ImageVector
){
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Songs : BottomBarScreen(
        route = "songs",
        title = "Songs",
        icon = Icons.Default.MusicNote
    )
    object Playlists : BottomBarScreen(
        route = "playlists",
        title = "Playlists",
        icon = Icons.Default.LibraryMusic
    )
    object Album : BottomBarScreen(
        route = "album",
        title = "Albums",
        icon = Icons.Default.Album
    )
}
