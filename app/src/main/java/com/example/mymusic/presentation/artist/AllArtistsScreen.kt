package com.example.mymusic.presentation.artist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumItem
import com.example.mymusic.presentation.history.SimpleTopBar
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.TOP_BAR_HEIGHT
import com.example.mymusic.presentation.songs.isScrolled
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllArtistsScreen(
    navController: NavController,
    currentPlayingAudio: Song?,
    artists : List<Artist>,
    addArtist: (Artist) -> Unit
    ){

    val scrollState = rememberLazyStaggeredGridState()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )
    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )

    Scaffold (
        topBar = {
            SimpleTopBar(navController, name = "All artists")
        }
    ){ padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingLazyList),
            contentPadding = PaddingValues(16.dp, bottom = animatedHeight),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {
            items(artists) { artist ->
                ArtistItem(navController = navController, artist = artist, addArtist = addArtist)
            }
        }
    }
}


@Composable
fun ArtistItem(
    navController: NavController,
    modifier: Modifier = Modifier,
    artist: Artist,
    addArtist : (Artist) -> Unit
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
                addArtist.invoke(artist)
                navController.navigate(Screen.ArtistDetailScreen.route)
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clip(CircleShape),
                imageModel = { R.mipmap.ic_launcher },
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
                    text = artist.artist,
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
                                Text(text = "Delete artist")
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
                                    text = "Delete artist",
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            },
                            text = {
                                Text(text = "Do you want to delete ${artist.artist} artist?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        //deleteAlbum(album)
                                        openDialog = false
                                    }) {
                                    Text("Delete")
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

        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}