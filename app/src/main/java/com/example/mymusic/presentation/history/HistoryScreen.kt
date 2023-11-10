package com.example.mymusic.presentation.history

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.AudioItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    history: History,
    navController: NavController,
    currentPlayingAudio: Song?,
    onItemClick: (Song) -> Unit,
    playlists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String) -> Unit,
    shareSong: (Song) -> Unit
) {


    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            SimpleTopBar(navController, name = "History")
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = animatedHeight),
                state = scrollState
            ) {
                items(
                    items = history.songs,
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
                        insertSongIntoPlaylist = insertSongIntoPlaylist,
                        shareSong = shareSong
                    )
                }

            }
        }
    }
}

@Composable
fun SimpleTopBar(
    navController: NavController,
    name: String
) {

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back button")
            }

            Text(
                text = name,
                modifier = Modifier.padding(start = 10.dp),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )

        }
    }
}