package com.example.mymusic.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.navigation.BottomBarScreen
import com.example.mymusic.presentation.navigation.BottomNavGraph
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.ArtistInfo
import com.example.mymusic.presentation.songs.MediaPlayerController

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun MainScreen(
    audioList: List<Song>,
    currentPlayingAudio: Song?,
    isAudioPlaying: Boolean,
    onStart: (Song) -> Unit,
    searchText: String,
    songs: List<Song>,
    onItemClick: (Song) -> Unit,
    onDataLoaded: () -> Unit,
) {

    val navController = rememberNavController()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp
    )
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(key1 = Unit) {
        onDataLoaded()
    }
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetContent = {
                currentPlayingAudio?.let { currentPlayingAudio ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = innerPadding.calculateBottomPadding() - 18.dp)
                            .clickable {
                                navController.navigate(Screen.PlayerScreen.route)
                            }
                    ) {
                        BottomBarPlayer(
                            song = currentPlayingAudio,
                            isAudioPlaying = isAudioPlaying,
                            onStart = { onStart.invoke(currentPlayingAudio) },
                        )
                    }

                }
            },
            sheetShape = RoundedCornerShape(topEnd = 26.dp, topStart = 26.dp),
            scaffoldState = scaffoldState,
            sheetPeekHeight = animatedHeight,
        ) {

            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                BottomNavGraph(
                    navController = navController,
                    songList = audioList,
                    songs = songs,
                    searchText = searchText,
                    currentPlayingAudio = currentPlayingAudio,
                    onItemClick = onItemClick
                )
            }
        }
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Songs,
        BottomBarScreen.Playlists,
        BottomBarScreen.Album,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
                clip = true
            }
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = screen.title)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun BottomBarPlayer(
    song: Song,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Gray),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArtistInfo(
            audio = song,
            modifier = Modifier
                .weight(1f)
        )
        MediaPlayerController(
            isAudioPlaying = isAudioPlaying,
            onStart = { onStart.invoke() },
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}