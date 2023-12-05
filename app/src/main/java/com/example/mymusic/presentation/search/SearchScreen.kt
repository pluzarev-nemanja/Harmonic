package com.example.mymusic.presentation.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.AudioItem
import com.example.mymusic.ui.theme.darkestBlueToWhite

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
    changeSongImage: (Song, String) -> Unit

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Songs ")
                        Divider(color = MaterialTheme.colors.darkestBlueToWhite)
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