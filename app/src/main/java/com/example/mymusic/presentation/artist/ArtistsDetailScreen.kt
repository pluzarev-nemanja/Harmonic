package com.example.mymusic.presentation.artist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumInfo
import com.example.mymusic.presentation.playlist.SongsList
import com.example.mymusic.presentation.playlist.TopBarPlaylist
import com.example.mymusic.presentation.songs.AudioItem
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ArtistsDetailScreen(
    navController : NavController,
    artist: Artist?,
    shuffle: (Artist) -> Unit,
    onStart: (Song, List<Song>) -> Unit,
    allPlaylists: List<Playlist>,
    insertSongIntoPlaylist: (Song, String) -> Unit,
    onItemClick: (Song) -> Unit,
    currentPlayingAudio: Song?,
) {

    Scaffold(
        topBar = {
            TopBarPlaylist(navController = navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ArtistInfo(
                artist = artist!!,
                shuffle = { shuffle.invoke(artist) },
                onStart = {
                    if(artist.songs.isNotEmpty())
                        onStart.invoke(artist.songs[0], artist.songs)
                }
            )
            SongsList(
                currentPlayingAudio = currentPlayingAudio,
                audioList = artist.songs,
                allPlaylists = allPlaylists,
                insertSongIntoPlaylist = insertSongIntoPlaylist,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun ArtistInfo(
    artist: Artist,
    shuffle: () -> Unit,
    onStart: () -> Unit,
) {

    val songsText by remember{
        if(artist.numberSongs > 1)
            mutableStateOf("songs")
        else mutableStateOf("song")
    }
    val albumText by remember{
        if(artist.numberAlbums > 1)
            mutableStateOf("albums")
        else mutableStateOf("album")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            imageModel = { R.mipmap.ic_launcher },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.DarkGray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = artist.artist,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Text(text = "${artist.numberAlbums} $albumText")
            Text(text = " · ${artist.numberSongs} $songsText")
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
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play songs"
                )
                Text(text = " Play")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Button(
                onClick = {
                    shuffle.invoke()
                },
                shape = CircleShape,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "shuffle",
                )
                Text(text = " Shuffle")

            }
        }
    }

}