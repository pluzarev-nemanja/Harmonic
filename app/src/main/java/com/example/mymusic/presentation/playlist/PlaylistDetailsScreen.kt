package com.example.mymusic.presentation.playlist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.AudioItem
import com.example.mymusic.presentation.songs.timeStampToDuration
import com.example.mymusic.ui.theme.lightBlueToWhite
import com.example.mymusic.ui.theme.whiteToDarkGrey
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun PlaylistDetailsScreen(
    currentPlayingAudio: Song?,
    navController: NavController,
    playlist: Playlist?,
    allPlaylists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String) -> Unit,
    onItemClick: (Song) -> Unit,
    shuffle: (Playlist) -> Unit,
    onStart: (Song, List<Song>) -> Unit,
    shareSong: (Song) -> Unit
) {

    Scaffold(
        topBar = {
            TopBarPlaylist(
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PlaylistInfo(
                playlist = playlist!!,
                shuffle = { shuffle.invoke(playlist) },
                onStart = {
                    if (playlist.songs.isNotEmpty())
                        onStart.invoke(playlist.songs[0], playlist.songs)
                },
            )
            SongsList(
                currentPlayingAudio = currentPlayingAudio,
                audioList = playlist.songs,
                allPlaylists = allPlaylists,
                insertSongIntoPlaylist = insertSongIntoPlaylist,
                onItemClick = onItemClick,
                shareSong = shareSong
            )
        }
    }
}

@Composable
fun PlaylistInfo(
    playlist: Playlist,
    shuffle: () -> Unit,
    onStart: () -> Unit,
) {

    Row(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        GlideImage(
            imageModel = { R.drawable.playlist },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.DarkGray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                text = playlist.playlistName
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${playlist.songCount} · ")
                Text(text = timeStampToDuration(playlist.playlistDuration))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        onStart.invoke()
                    },
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.lightBlueToWhite)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play playlist",
                        tint = MaterialTheme.colors.whiteToDarkGrey
                    )
                    Text(text = " Play",
                        color = MaterialTheme.colors.whiteToDarkGrey)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = {
                        shuffle.invoke()
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "shuffle",
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsList(
    currentPlayingAudio: Song?,
    audioList: List<Song>,
    allPlaylists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String) -> Unit,
    onItemClick: (Song) -> Unit,
    shareSong: (Song) -> Unit
) {

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = animatedHeight),
    ) {
        items(audioList) { song ->
            AudioItem(
                audio = song,
                onItemClick = { onItemClick.invoke(song) },
                modifier = Modifier.animateItemPlacement(
                    tween(durationMillis = 450)
                ),
                playlists = allPlaylists,
                insertSongIntoPlaylist = insertSongIntoPlaylist,
                shareSong = shareSong
            )
        }
    }
}


@Composable
fun TopBarPlaylist(
    navController: NavController
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back button")
            }

            IconButton(onClick = {
                //add more options to playlist

            }) {
                Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "More options")
            }
        }
    }
}