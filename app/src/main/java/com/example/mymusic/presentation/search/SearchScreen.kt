package com.example.mymusic.presentation.search

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.AudioItem
import com.example.mymusic.ui.theme.darkestBlueToWhite
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    searchText: String,
    songs: List<Song>,
    currentPlayingAudio: Song?,
    onItemClick: (Song) -> Unit,
    playlists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String, String) -> Unit,
    onSearchTextChange: (String) -> Unit,
    shareSong: (Song) -> Unit,
    changeSongImage: (Song, String) -> Unit,
    artists: List<Artist>,
    albums: List<Album>,
    navController: NavController,
    addAlbum: (Album) -> Unit,
    addArtist: (Artist) -> Unit,
) {

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = ""
    )

    val scrollState = rememberLazyListState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchAppBar(
            searchText,
            onSearchTextChange = onSearchTextChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (searchText.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = "No results...", modifier = Modifier.align(Alignment.Center))
                OpenKeyboardExample(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = animatedHeight)
                )
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (songs.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(text = "Songs ")
                            Divider(color = MaterialTheme.colors.darkestBlueToWhite)
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = animatedHeight),
                        state = scrollState
                    ) {
                        items(
                            items = songs,
                            key = { song ->
                                song.id
                            }
                        ) { song: Song ->
                            AudioItem(
                                audio = song,
                                onItemClick = { onItemClick.invoke(song) },
                                modifier = Modifier.animateItemPlacement(
                                    tween(durationMillis = 250)
                                ),
                                playlists = playlists,
                                insertSongIntoPlaylist = insertSongIntoPlaylist,
                                shareSong = shareSong,
                                changeSongImage
                            )
                        }
                        items(
                            items = albums,
                            key = { album ->
                                album.albumId
                            }
                        ) { album: Album ->
                            if (albums.isNotEmpty() && albums.first() == album) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(text = "Albums ")
                                    Divider(color = MaterialTheme.colors.darkestBlueToWhite)
                                }
                            }
                            AlbumItem(
                                navController = navController,
                                image = album.albumImage,
                                album = album,
                                addAlbum = addAlbum,
                                modifier = Modifier.animateItemPlacement(
                                    tween(durationMillis = 250)
                                ),
                            )
                        }

                        items(
                            items = artists,
                            key = { artist ->
                                artist.artist
                            }
                        ) { artist: Artist ->
                            if (artists.isNotEmpty() && artists.first() == artist) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(text = "Artists ")
                                    Divider(color = MaterialTheme.colors.darkestBlueToWhite)
                                }
                            }
                            ArtistItem(
                                artist = artist,
                                navController = navController,
                                image = artist.artistImage,
                                addArtist = addArtist,
                                modifier = Modifier.animateItemPlacement(
                                    tween(durationMillis = 250)
                                ),
                            )
                        }

                    }
                }
                OpenKeyboardExample(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = animatedHeight)
                )
            }
        }
    }
}

@Composable
fun ArtistItem(
    artist: Artist,
    navController: NavController,
    image: String,
    addArtist: (Artist) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {
                    addArtist.invoke(artist)
                    navController.navigate(Screen.ArtistDetailScreen.route)
                },
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = .5f),
            elevation = 0.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                GlideImage(
                    imageModel = { if (image != "") Uri.parse(image) else R.drawable.artist },
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
                        text = artist.artist,
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = artist.numberSongs.toString(),
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors
                                .onSurface
                                .copy(alpha = .5f)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }

        }

    }
}

@Composable
fun AlbumItem(
    navController: NavController,
    image: String,
    album: Album,
    addAlbum: (Album) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {
                    addAlbum.invoke(album)
                    navController.navigate(Screen.AlbumDetailScreen.route)
                },
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = .5f),
            elevation = 0.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                GlideImage(
                    imageModel = { if (image != "") Uri.parse(image) else R.drawable.album },
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
                        text = album.albumName,
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = album.artist,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors
                                .onSurface
                                .copy(alpha = .5f)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }

        }

    }

}

@Composable
fun SearchAppBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchText,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(12.dp),
        onValueChange = {
            onSearchTextChange.invoke(it)
        },
        placeholder = { Text(text = "Search...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.darkestBlueToWhite
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                onSearchTextChange.invoke("")
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colors.darkestBlueToWhite
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.darkestBlueToWhite,
            focusedBorderColor = MaterialTheme.colors.darkestBlueToWhite,
            cursorColor = MaterialTheme.colors.darkestBlueToWhite
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OpenKeyboardExample(modifier: Modifier) {
    val kc = LocalSoftwareKeyboardController.current
    Button(
        onClick = { kc?.show() },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Keyboard, contentDescription = "open keyboard")
        Spacer(Modifier.height(4.dp))
        Text(text = "Keyboard")
    }
}