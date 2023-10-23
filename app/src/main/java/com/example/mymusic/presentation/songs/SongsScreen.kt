package com.example.mymusic.presentation.songs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.util.SortOrder
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.util.Marquee
import com.example.mymusic.presentation.util.defaultMarqueeParams
import com.example.mymusic.ui.theme.bottomBarColor
import com.example.mymusic.ui.theme.heartColor
import com.example.mymusic.ui.theme.textColor
import com.example.mymusic.ui.theme.textOnBottomBar
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.floor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsScreen(
    isAudioPlaying: Boolean,
    audioList: List<Song>,
    currentPlayingAudio: Song?,
    onItemClick: (Song) -> Unit,
    sortOrderChange: (SortOrder) -> Unit,
    navController: NavController,
    playlists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String) -> Unit
) {

    val sortOrder by remember {
        mutableStateOf(SortOrder.ASCENDING)
    }

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    val scrollState = rememberLazyListState()

    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )

    val scrollKnobPadding by animateDpAsState(
        targetValue = if (scrollState.isScrolled || isAudioPlaying) 80.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "scrollKnobPadding"
    )


    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = animatedHeight),
                    modifier = Modifier
                        .padding(top = paddingLazyList),
                    state = scrollState
                ) {
                    items(
                        items = audioList,
//                        key = {
//                            it.id
//                        }
                    ) { song: Song ->
                        AudioItem(
                            audio = song,
                            onItemClick = { onItemClick.invoke(song) },
                            modifier = Modifier.animateItemPlacement(
                                tween(durationMillis = 450)
                            ),
                            playlists = playlists,
                            insertSongIntoPlaylist = insertSongIntoPlaylist
                        )
                    }

                }
                TopBar(
                    lazyListState = scrollState,
                    sortOrder,
                    onSortOrderChange = { sortOrderChange.invoke(it) },
                    navController = navController
                )
            }
        }
    )
}

@Composable
fun TopBar(
    lazyListState: LazyListState,
    sortOrdering: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
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
                //navigate to search screen
                navController.navigate(Screen.SearchScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search button")
            }
            Text(
                text = "Songs",
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
                            sortOrder.value = SortOrder.ASCENDING
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Ascending",
                                isSelected = sortOrder.value == SortOrder.ASCENDING
                            ) {
                                sortOrder.value = SortOrder.ASCENDING
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = SortOrder.DESCENDING
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Descending",
                                isSelected = sortOrder.value == SortOrder.DESCENDING
                            ) {
                                sortOrder.value = SortOrder.DESCENDING
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = SortOrder.ARTIST
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Artist",
                                isSelected = sortOrder.value == SortOrder.ARTIST
                            ) {
                                sortOrder.value = SortOrder.ARTIST
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = SortOrder.ALBUM
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Album",
                                isSelected = sortOrder.value == SortOrder.ALBUM
                            ) {
                                sortOrder.value = SortOrder.ALBUM
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = SortOrder.DATE_ADDED
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Date added",
                                isSelected = sortOrder.value == SortOrder.DATE_ADDED
                            ) {
                                sortOrder.value = SortOrder.DATE_ADDED
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

@Composable
fun SortOrderItem(
    sortOrder: String,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = sortOrder,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1
        )
        RadioButton(selected = isSelected, onClick = { onItemClick.invoke() })
    }
}


@Composable
fun AudioItem(
    audio: Song,
    onItemClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    playlists: List<Playlist> = emptyList(),
    insertSongIntoPlaylist: (Song, String) -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }
    var openDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                onItemClick.invoke(audio.id)
            },
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = .5f),
        elevation = 0.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            GlideImage(
                imageModel = { audio.artUri },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .size(60.dp)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))

            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = audio.displayName,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = audio.artist,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors
                            .onSurface
                            .copy(alpha = .5f)
                    )

                    Text(
                        text = " · " + timeStampToDuration(audio.duration.toLong()),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors
                            .onSurface
                            .copy(alpha = .5f),
                        modifier = Modifier
                            .weight(1f),
                        maxLines = 1,
                    )
                }
            }
            //here comes code for opening menu
            IconButton(onClick = {
                showMenu = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More options",
                    tint = MaterialTheme.colors
                        .onSurface
                        .copy(alpha = .5f),

                    )
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
                            Text(
                                text = "Add song to playlist",

                                )
                        }
                    }
                }
                if (openDialog) {

                    var selectedIndex by remember {
                        mutableStateOf("")
                    }

                    AlertDialog(
                        modifier = Modifier.fillMaxHeight(0.4f),
                        shape = RoundedCornerShape(10.dp),
                        onDismissRequest = {
                            openDialog = false
                        },
                        title = {
                            Text(
                                text = "Choose playlist",
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        },
                        text = {
                            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                                items(playlists) { playlist ->
                                    PlaylistChooser(playlist = playlist,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .selectable(
                                                selected = selectedIndex == playlist.playlistName,
                                                onClick = {
                                                    selectedIndex =
                                                        if (selectedIndex == playlist.playlistName) "" else playlist.playlistName
                                                }
                                            )
                                            .background(
                                                if (selectedIndex == playlist.playlistName) Color.Gray
                                                else Color.Transparent
                                            )
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    //here comes function for updating data in playlist,pass selectedIndex as name
                                    insertSongIntoPlaylist.invoke(audio, selectedIndex)
                                    openDialog = false
                                }) {
                                Text("Add")
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
            Spacer(modifier = Modifier.size(8.dp))
        }

    }
}

@Composable
fun PlaylistChooser(
    playlist: Playlist,
    modifier: Modifier = Modifier
) {
    Column {

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = playlist.playlistName,
            fontSize = 15.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

fun timeStampToDuration(position: Long): String {
    val totalSeconds = floor(position / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)

    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)


}


@Composable
fun MediaPlayerController(
    modifier: Modifier = Modifier,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    addFavorite: (Song) -> Unit,
    song: Song,
    isSelected: Boolean
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(60.dp)
            .padding(1.dp)
            .padding(bottom = 10.dp)
    ) {

        IconButton(onClick = { addFavorite.invoke(song) }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "favorite",
                tint = if (isSelected) Color.Red
                else MaterialTheme.colors.heartColor,
                modifier = Modifier
                    .padding(end = 2.dp)
            )
        }
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(50.dp)
        ) {
            onStart.invoke()
        }
    }
}


@Composable
fun ArtistInfo(
    modifier: Modifier = Modifier,
    audio: Song
) {
    Row(
        modifier = modifier.padding(start = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        GlideImage(
            imageModel = { audio.artUri },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier
                .size(height = 90.dp, width = 100.dp)
                .clip(RoundedCornerShape(topStart = 8.dp))
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    val colors = listOf(Color.Black, Color.Transparent)
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(colors, startX = 200f),
                        blendMode = BlendMode.DstIn
                    )
                }
        )
        Spacer(modifier = Modifier.size(4.dp))

        Column {
            Marquee(
                modifier = Modifier,
                params = defaultMarqueeParams(),
            ) {
                Text(
                    text = audio.title,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.textOnBottomBar
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = audio.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 10.sp,
                color = MaterialTheme.colors.textOnBottomBar
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}


@Composable
fun PlayerIconItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    border: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colors.surface,
    color: Color = MaterialTheme.colors.onSurface,
    onClick: () -> Unit,
) {

    Surface(
        shape = RoundedCornerShape(15.dp),
        border = border,
        modifier = modifier
            .clickable {
                onClick.invoke()
            },
        contentColor = color,
        color = backgroundColor

    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )

        }


    }


}

val TOP_BAR_HEIGHT = 56.dp
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

@OptIn(ExperimentalFoundationApi::class)
val LazyStaggeredGridState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

//fun LazyListState.isScrolledToTheEnd() =
//    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
