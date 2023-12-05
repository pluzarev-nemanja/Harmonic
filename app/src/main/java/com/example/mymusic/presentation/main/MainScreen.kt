package com.example.mymusic.presentation.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.navigation.BottomBarScreen
import com.example.mymusic.presentation.navigation.BottomNavGraph
import com.example.mymusic.presentation.player.PlayerScreen
import com.example.mymusic.presentation.player.SheetCollapsed
import com.example.mymusic.presentation.player.SheetContent
import com.example.mymusic.presentation.player.SheetExpanded
import com.example.mymusic.presentation.playlist.PlaylistViewModel
import com.example.mymusic.presentation.songs.ArtistInfo
import com.example.mymusic.presentation.songs.MediaPlayerController
import com.example.mymusic.presentation.util.currentFraction
import com.example.mymusic.ui.theme.darkestBlueToWhite
import kotlinx.coroutines.launch

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
    onDataLoaded: () -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    skipNext: () -> Unit,
    skipPrevious: () -> Unit,
    shuffle: () -> Unit,
    repeat: (Int) -> Unit,
    updateTimer: () -> String,
    addFavorite: (Song) -> Unit,
    equalizer: () -> Unit,
    shareSong: (Song) -> Unit,
    isSnowing: Boolean,
    playlistViewModel: PlaylistViewModel = hiltViewModel(),
    changeSongImage: (Song, String) -> Unit
) {

    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )


    val sheetToggle: () -> Unit = {
        coroutineScope.launch {
            if (scaffoldState.bottomSheetState.isCollapsed) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        onDataLoaded()
    }

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }


    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 115.dp, label = "height animation"
    )


    val radius = 30.dp


    var isSelected by remember {
        mutableStateOf(currentPlayingAudio?.isFavorite)
    }


    Scaffold(bottomBar = {
        BottomBar(navController = navController, bottomBarState = bottomBarState)
    }) { innerPadding ->
        BottomSheetScaffold(
            sheetContent = {
                SheetContent {
                    SheetExpanded {
                        currentPlayingAudio?.let { currentPlayingAudio ->
                            isSelected = currentPlayingAudio.isFavorite
                            PlayerScreen(
                                songName = currentPlayingAudio.displayName,
                                artist = currentPlayingAudio.artist,
                                close = { sheetToggle() },
                                progress = progress,
                                onProgressChange = onProgressChange,
                                audio = currentPlayingAudio,
                                isAudioPlaying = isAudioPlaying,
                                onStart = { onStart.invoke(currentPlayingAudio) },
                                skipNext = { skipNext.invoke() },
                                skipPrevious = { skipPrevious.invoke() },
                                shuffle = { shuffle.invoke() },
                                repeat = repeat,
                                updateTimer = { updateTimer.invoke() },
                                addFavorite = {
                                    addFavorite.invoke(currentPlayingAudio)
                                    isSelected = !isSelected!!
                                },
                                isSelected = isSelected!!,
                                isSnowing = isSnowing,
                                playlists = playlistViewModel.playlists,
                                insertSongIntoPlaylist = { song,name,img ->
                                    playlistViewModel.insertSongIntoPlaylist(song,name,img)
                                },
                                shareSong = shareSong,
                                changeSongImage = changeSongImage,
                            )
                        }
                    }

                    SheetCollapsed(
                        isCollapsed = scaffoldState.bottomSheetState.isCollapsed,
                        currentFraction = scaffoldState.currentFraction,
                        onSheetClick = sheetToggle,
                    ) {
                        currentPlayingAudio?.let { currentPlayingAudio ->
                            isSelected = currentPlayingAudio.isFavorite
                            BottomBarPlayer(
                                song = currentPlayingAudio,
                                isAudioPlaying = isAudioPlaying,
                                onStart = { onStart.invoke(currentPlayingAudio) },
                                addFavorite = {
                                    addFavorite.invoke(currentPlayingAudio)
                                    isSelected = !isSelected!!
                                },
                                isSelected = isSelected
                            )
                        }
                    }
                }
            },
            sheetShape = RoundedCornerShape(topEnd = radius, topStart = radius),
            scaffoldState = scaffoldState,
            sheetPeekHeight = animatedHeight,
        ) {

            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                BottomNavGraph(
                    navController = navController,
                    songList = audioList,
                    songs = songs,
                    searchText = searchText,
                    currentPlayingAudio = currentPlayingAudio,
                    equalizer = equalizer,
                    shareSong = shareSong
                )
            }
        }
    }
}


@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Songs,
        BottomBarScreen.Playlists,
        BottomBarScreen.Album,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(modifier = Modifier.graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 17.dp, topEnd = 17.dp
                )
                clip = true
            }) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        })
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen, currentDestination: NavDestination?, navController: NavHostController
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
        },
        alwaysShowLabel = false,

        )
}

@Composable
fun BottomBarPlayer(
    song: Song,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    addFavorite: (Song) -> Unit,
    isSelected: Boolean?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.darkestBlueToWhite),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArtistInfo(
            audio = song, modifier = Modifier.weight(1f)
        )
        MediaPlayerController(
            isAudioPlaying = isAudioPlaying,
            onStart = { onStart.invoke() },
            addFavorite = addFavorite,
            song = song,
            isSelected = isSelected!!
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}