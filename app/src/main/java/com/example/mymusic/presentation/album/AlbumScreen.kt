package com.example.mymusic.presentation.album

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.util.AlbumSortOrder
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.SortOrderItem
import com.example.mymusic.presentation.songs.TOP_BAR_HEIGHT
import com.example.mymusic.presentation.songs.isScrolled
import com.example.mymusic.ui.theme.whiteToDarkGrey
import com.example.mymusic.ui.theme.lightBlueToWhite
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(
    currentPlayingAudio: Song?,
    navController: NavController,
    albums: List<Album>,
    sortOrderChange: (AlbumSortOrder) -> Unit,
    addAlbum: (Album) -> Unit,
    changeAlbumImage : (Album,String) -> Unit
) {
    val sortOrder by remember {
        mutableStateOf(AlbumSortOrder.ALBUM_NAME)
    }

    val scrollState = rememberLazyStaggeredGridState()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )
    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )

    Scaffold { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingLazyList),
            contentPadding = PaddingValues(16.dp, bottom = animatedHeight),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {

            items(albums) { album ->
                AlbumItem(
                    album = album,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    navController,
                    addAlbum = addAlbum,
                    changeAlbumImage = changeAlbumImage
                )
            }
        }
        AlbumTopBar(
            lazyListState = scrollState,
            sortOrdering = sortOrder,
            onSortOrderChange = {
                sortOrderChange.invoke(it)
            },
            navController = navController
        )
    }
}


@Composable
fun AlbumItem(
    album: Album,
    modifier: Modifier = Modifier,
    navController: NavController,
    addAlbum: (Album) -> Unit,
    changeAlbumImage: (Album, String) -> Unit
) {

    var showMenu by remember {
        mutableStateOf(false)
    }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            //here update that in database user image
            changeAlbumImage.invoke(album,uri.toString())
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                addAlbum.invoke(album)
                navController.navigate(Screen.AlbumDetailScreen.route)
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                modifier = Modifier
                    .padding(5.dp)
                    .size(150.dp)
                    .clip(CircleShape),
                imageModel = { if (album.albumImage != "") album.albumImage
                else R.drawable.album },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
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
                    text = album.albumName,
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
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                                showMenu = false
                            }) {
                                Text(text = "Change image")
                            }
                        }
                    }
                    Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "More options")
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = album.artist)
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumTopBar(
    lazyListState: LazyStaggeredGridState,
    sortOrdering: AlbumSortOrder,
    onSortOrderChange: (AlbumSortOrder) -> Unit,
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
                text = "Albums",
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
                            navController.navigate(Screen.SettingsScreen.route)
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
                            sortOrder.value = AlbumSortOrder.ALBUM_NAME
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Album Name",
                                isSelected = sortOrder.value == AlbumSortOrder.ALBUM_NAME
                            ) {
                                sortOrder.value = AlbumSortOrder.ALBUM_NAME
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = AlbumSortOrder.ARTIST
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Artist",
                                isSelected = sortOrder.value == AlbumSortOrder.ARTIST
                            ) {
                                sortOrder.value = AlbumSortOrder.ARTIST
                                onSortOrderChange.invoke(sortOrder.value)
                                showNestedMenu = false
                            }
                        }
                        DropdownMenuItem(onClick = {
                            sortOrder.value = AlbumSortOrder.SONG_COUNT
                            onSortOrderChange.invoke(sortOrder.value)
                            showNestedMenu = false
                        }) {
                            SortOrderItem(
                                sortOrder = "Song count",
                                isSelected = sortOrder.value == AlbumSortOrder.SONG_COUNT
                            ) {
                                sortOrder.value = AlbumSortOrder.SONG_COUNT
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