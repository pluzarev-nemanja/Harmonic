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
import androidx.compose.material.ButtonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.AudioItem
import com.example.mymusic.presentation.util.shadow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun PlaylistDetailsScreen(
    currentPlayingAudio: Song?,
    navController : NavController,
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
            PlaylistInfo()
            SongsList(
                currentPlayingAudio = currentPlayingAudio
            )
        }
    }
}

@Composable
fun PlaylistInfo() {
    Row (
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .padding(15.dp)
    ){
        GlideImage(
            imageModel = { R.mipmap.ic_launcher },
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
                text = "Playlist name.."
            )
            Row(modifier = Modifier.fillMaxWidth()) {
               Text(text = "283 songs · ")
               Text(text = "16:28:22")
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                   Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play playlist")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = { /*TODO*/ },
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Shuffle, contentDescription = "Play playlist")
                }
            }
        }
    }
}

val songs = listOf(
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    ),
    Song(
        "ss",
        "ss",
        "ss",
        1,
        "",
        "",
        "",
        "",
        "",
        1L
    )
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsList(
    currentPlayingAudio: Song?,
    ) {

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = animatedHeight),
        ){
        items(songs){ song ->
            SongItem(song = song)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SongItem(
    modifier : Modifier = Modifier,
    song : Song
) {
    
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Magenta),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
       Icon(imageVector = Icons.Filled.MusicNote, contentDescription = "") 
        Text(text = "Song name...")
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
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back button")
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "Back button")
            }
        }
    }
}