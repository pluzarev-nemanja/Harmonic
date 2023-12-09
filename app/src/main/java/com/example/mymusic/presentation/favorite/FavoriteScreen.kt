package com.example.mymusic.presentation.favorite

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.history.SimpleTopBar
import com.example.mymusic.presentation.songs.AudioItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    favorite: List<Song>,
    currentPlayingAudio: Song?,
    onItemClick: (Song,List<Song>) -> Unit,
    playlists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String, String) -> Unit,
    shareSong: (Song) -> Unit,
    changeSongImage: (Song, String) -> Unit

) {

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            SimpleTopBar(navController, name = "Favorite")
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = animatedHeight),
                state = scrollState
            ) {
                items(
                    items = favorite,
//                        key = {
//                            it.id
//                        }
                ) { song: Song ->
                    AudioItem(
                        audio = song,
                        onItemClick = { onItemClick.invoke(song,favorite) },
                        modifier = Modifier.animateItemPlacement(
                            tween(durationMillis = 450)
                        ),
                        playlists = playlists,
                        insertSongIntoPlaylist = insertSongIntoPlaylist,
                        shareSong = shareSong,
                        changeSongImage = changeSongImage
                    )
                }

            }
        }
    }
}