package com.example.mymusic.presentation.playlist

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.util.PlaylistSortOrder
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.SortOrderItem
import com.example.mymusic.presentation.songs.TOP_BAR_HEIGHT
import com.example.mymusic.presentation.songs.isScrolled
import com.example.mymusic.presentation.songs.timeStampToDuration
import com.example.mymusic.presentation.util.shadow
import com.example.mymusic.ui.theme.whiteToDarkGrey
import com.example.mymusic.ui.theme.lightBlueToWhite
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    playlists : List<Playlist>,
    sortOrderChange: (PlaylistSortOrder) -> Unit,
    navController: NavController,
    currentPlayingAudio: Song?,
    deletePlaylist: (Playlist) -> Unit,
    addPlaylist: (Playlist) -> Unit,
    insertPlaylist : (String) -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }
    var filledText by remember {
        mutableStateOf("")
    }
    val scrollState = rememberLazyStaggeredGridState()

    val sortOrder by remember {
        mutableStateOf(PlaylistSortOrder.ASCENDING)
    }
    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openDialog = true

                },
                modifier = Modifier.padding(bottom = animatedHeight)
            ) {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = "Add new playlist",
                    tint = MaterialTheme.colors.whiteToDarkGrey
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingLazyList),
            contentPadding = PaddingValues(16.dp, bottom = animatedHeight),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {
            items(
                items = playlists,
                key = {
                    it.playlistName
                }
            ) { playlist: Playlist ->
                PlaylistItem(
                    playlist = playlist,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    deletePlaylist = deletePlaylist,
                    navController = navController,
                    addPlaylist = addPlaylist
                )
            }
        }
        TopBarPlaylist(
            lazyListState = scrollState,
            sortOrder,
            onSortOrderChange = { sortOrderChange.invoke(it) },
            navController = navController
        )

        if (openDialog) {
            AlertDialog(
                shape = RoundedCornerShape(10.dp),
                onDismissRequest = {
                    openDialog = false
                },
                title = {
                    Text(
                        text = "New Playlist"
                    )
                },
                text = {
                    OutlinedTextField(
                        value = filledText,
                        onValueChange = {
                            filledText = it
                        },
                        label = {
                            Text(text = "Enter playlist name")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog = false
                            insertPlaylist.invoke(filledText)
                            filledText = ""
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.lightBlueToWhite)
                    ) {
                        Text("Create playlist",
                        color = MaterialTheme.colors.whiteToDarkGrey
                        )
                    }
                },
                dismissButton = {

                    Button(
                        onClick = {
                            openDialog = false
                            filledText = ""
                        },
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }


}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    modifier: Modifier = Modifier,
    deletePlaylist: (Playlist) -> Unit,
    navController: NavController,
    addPlaylist : (Playlist) -> Unit
    ) {

    var showMenu by remember {
        mutableStateOf(false)
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable {
               addPlaylist.invoke(playlist)
                navController.navigate(Screen.PlaylistDetailsScreen.route)
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                imageModel = { R.drawable.playlist },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .shadow(offsetY = 130.dp, blurRadius = 15.dp)
                    .background(Color.DarkGray),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = playlist.playlistName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    showMenu = true
                }) {
                    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {
                                showMenu = false
                            },
                        ) {
                            DropdownMenuItem(onClick = {
                                openDialog = true
                                showMenu = false
                            }) {
                                Text(text = "Delete playlist")
                            }
                        }
                    }
                    Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "More options")
                    if (openDialog) {

                        AlertDialog(
                            shape = RoundedCornerShape(10.dp),
                            onDismissRequest = {
                                openDialog = false
                            },
                            title = {
                                Text(
                                    text = "Delete playlist",
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            },
                            text = {
                                Text(text = "Do you want to delete ${playlist.playlistName} playlist?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        deletePlaylist(playlist)
                                        openDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.lightBlueToWhite)
                                ) {
                                    Text("Delete",
                                        color = MaterialTheme.colors.whiteToDarkGrey
                                    )
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        openDialog = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${playlist.songCount} songs ")
                Text(text = "· ${timeStampToDuration(playlist.playlistDuration)}")
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarPlaylist(
    lazyListState: LazyStaggeredGridState,
    sortOrdering: PlaylistSortOrder,
    onSortOrderChange: (PlaylistSortOrder) -> Unit,
    navController: NavController
) {

    var showMenu by remember { mutableStateOf(false) }
    var showNestedMenu by remember { mutableStateOf(false) }
    val sortOrder = remember {
        mutableStateOf(sortOrdering)
    }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .animateContentSize(animationSpec = tween(durationMillis = 300))
            .height(height = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT),

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.navigate(Screen.SearchScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search button")
            }
            Text(
                text = "Playlists",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
            Box(modifier = Modifier.padding(end = 8.dp)) {
                IconButton(onClick = {
                    showMenu = !showMenu
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More options",
                        tint = MaterialTheme.colors
                            .onSurface
                            .copy(alpha = .5f)
                    )
                }
                MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {

                        DropdownMenuItem(onClick = {
                            showMenu = false
                            showNestedMenu = true
                        }) {
                            Text(text = "Sort order ")
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Sort order"
                            )
                        }

                        DropdownMenuItem(onClick = {
                            //Navigation to Settings Screen
                            showMenu = false
                        }) {
                            Text(text = "Settings")
                        }

                    }
                    DropdownMenu(
                        expanded = showNestedMenu,
                        onDismissRequest = { showNestedMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {}) {
                            Text(
                                text = "Sort order",
                                color = MaterialTheme.colors
                                    .onSurface
                                    .copy(alpha = .5f),
                                style = MaterialTheme.typography.body1,
                                fontSize = 13.sp
                            )
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = PlaylistSortOrder.ASCENDING
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Ascending",
                                isSelected = sortOrder.value == PlaylistSortOrder.ASCENDING
                            ) {
                                sortOrder.value = PlaylistSortOrder.ASCENDING
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = PlaylistSortOrder.DESCENDING
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Descending",
                                isSelected = sortOrder.value == PlaylistSortOrder.DESCENDING
                            ) {
                                sortOrder.value = PlaylistSortOrder.DESCENDING
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = PlaylistSortOrder.SONG_COUNT
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Song count",
                                isSelected = sortOrder.value == PlaylistSortOrder.SONG_COUNT
                            ) {
                                sortOrder.value = PlaylistSortOrder.SONG_COUNT
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = PlaylistSortOrder.DURATION
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Duration",
                                isSelected = sortOrder.value == PlaylistSortOrder.DURATION
                            ) {
                                sortOrder.value = PlaylistSortOrder.DURATION
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                    }
                }
            }
        }
    }
}