package com.example.mymusic.presentation.songs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.util.SortOrder
import com.example.mymusic.presentation.util.Marquee
import com.example.mymusic.presentation.util.defaultMarqueeParams
import com.example.mymusic.presentation.util.scrollbar
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.floor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongsScreen(
    isAudioPlaying: Boolean,
    audioList: List<Song>,
    currentPlayingAudio: Song?,
    onStart: (Song) -> Unit,
    onItemClick: (Song) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp
    )

    val scrollState = rememberLazyListState()

    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300)
    )

    val scrollKnobPadding by animateDpAsState(
        targetValue = if (scrollState.isScrolled || isAudioPlaying) 80.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300)
    )

    BottomSheetScaffold(
        sheetContent = {
            currentPlayingAudio?.let { currentPlayingAudio ->

                BottomBarPlayer(
                    song = currentPlayingAudio,
                    isAudioPlaying = isAudioPlaying,
                    onStart = { onStart.invoke(currentPlayingAudio) }
                )

            }
        },
        sheetShape = RoundedCornerShape(topEnd = 26.dp, topStart = 26.dp),
        scaffoldState = scaffoldState,
        sheetPeekHeight = animatedHeight
    ) {
        //here top bar
        Scaffold(
            content = { padding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = animatedHeight),
                        modifier = Modifier
                            .scrollbar(
                                scrollState,
                                thickness = 10.dp,
                                knobColor = Color.Cyan,
                                trackColor = Color.LightGray,
                                padding = scrollKnobPadding,
                                fixedKnobRatio = 0.03f
                            )
                            .padding(top = paddingLazyList),
                        state = scrollState
                    ) {
                        items(
                            items = audioList
                        ) { song: Song ->
                            AudioItem(
                                audio = song,
                                onItemClick = { onItemClick.invoke(song) },
                            )
                        }

                    }
                    TopBar(lazyListState = scrollState)
                }
            }
        )
    }
}

@Composable
fun TopBar(lazyListState: LazyListState) {

    var showMenu by remember { mutableStateOf(false) }
    var showNestedMenu by remember { mutableStateOf(false) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASCENDING) }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .animateContentSize(animationSpec = tween(durationMillis = 300))
            .height(height = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT),
        contentPadding = PaddingValues(start = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search button")
            }
            Text(
                text = "Songs",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    color = MaterialTheme.colors.surface,
                    fontStyle = FontStyle.Italic
                )
            )
            Box(modifier = Modifier.padding(end = 12.dp)) {
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
                            sortOrder = SortOrder.ASCENDING
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Ascending",
                                isSelected = sortOrder == SortOrder.ASCENDING
                            ){
                                sortOrder = SortOrder.ASCENDING
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder = SortOrder.DESCENDING
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Descending",
                                isSelected = sortOrder == SortOrder.DESCENDING
                            ){
                                sortOrder = SortOrder.DESCENDING
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder = SortOrder.ARTIST
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Artist",
                                isSelected = sortOrder == SortOrder.ARTIST
                            ){
                                sortOrder = SortOrder.ARTIST
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder = SortOrder.ALBUM
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Album",
                                isSelected = sortOrder == SortOrder.ALBUM
                            ){
                                sortOrder = SortOrder.ALBUM
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder = SortOrder.DATE_ADDED
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Date added",
                                isSelected = sortOrder == SortOrder.DATE_ADDED
                            ){
                                sortOrder = SortOrder.DATE_ADDED
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
    onItemClick : () -> Unit
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
        RadioButton(selected = isSelected, onClick = {onItemClick.invoke()})
    }
}


@Composable
fun AudioItem(
    audio: Song,
    onItemClick: (id: Long) -> Unit
) {
    Card(
        modifier = Modifier
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
                    .padding(8.dp)
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
                            .copy(alpha = .5f)
                    )
                }
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More options",
                    tint = MaterialTheme.colors
                        .onSurface
                        .copy(alpha = .5f),

                    )
            }
            Spacer(modifier = Modifier.size(8.dp))
        }

    }


}

private fun timeStampToDuration(position: Long): String {
    val totalSeconds = floor(position / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)

    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)


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
            .height(70.dp),
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

@Composable
fun MediaPlayerController(
    modifier: Modifier = Modifier,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(60.dp)
            .padding(1.dp)
    ) {
        PlayerIconItem(
            icon = Icons.Default.Favorite,
            color = Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 6.dp)
        ) {
            //on like button click
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
        modifier = modifier.padding(start = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

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
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = audio.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 10.sp
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
    onClick: () -> Unit
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
            )

        }


    }


}

val TOP_BAR_HEIGHT = 56.dp
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1