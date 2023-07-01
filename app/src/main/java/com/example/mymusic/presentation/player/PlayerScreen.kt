package com.example.mymusic.presentation.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.PlayerIconItem
import com.example.mymusic.presentation.songs.timeStampToDuration
import com.example.mymusic.presentation.util.Marquee
import com.example.mymusic.presentation.util.defaultMarqueeParams
import com.example.mymusic.presentation.util.shadow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PlayerScreen(
    image : String,
    songName : String,
    artist : String,
    close : () -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    audio : Song,
    isAudioPlaying: Boolean,
    onStart: (Song) -> Unit,
    skipNext: () -> Unit,
    skipPrevious: () -> Unit,
    shuffle: () -> Unit,
    repeat : (Int) -> Unit
) {

    Surface(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(close)
            Spacer(modifier = Modifier.height(18.dp))
            GlideImage(
                imageModel = { image },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .shadow(offsetY = 230.dp, blurRadius = 15.dp)
                    .size(300.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.DarkGray),
            )
            Spacer(modifier = Modifier.height(38.dp))
            SongInfo(songName, artist)
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(progress = progress,
                onProgressChange = onProgressChange,
            audio = audio
                )
            Spacer(modifier = Modifier.height(38.dp))
            PlayerControls(
                isAudioPlaying = isAudioPlaying,
                onStart = { onStart.invoke(audio) },
                skipNext = { skipNext.invoke() },
                skipPrevious = { skipPrevious.invoke() },
                shuffle = {shuffle.invoke()},
                repeat = repeat
            )
            Spacer(modifier = Modifier.height(18.dp))
            MoreOptions()
        }
    }
}


@Composable
fun Header(
    close : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            close()
        }) {
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "back",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),
            )
        }
        Text(
            modifier = Modifier
                .align(CenterVertically),
            text = "Now Playing",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More options",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
        }
    }
}

@Composable
fun SongInfo(
    songName : String,
    artist : String
) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Marquee(
            params = defaultMarqueeParams(),
        ) {
                Text(
                    text = songName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = artist,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp
            )
        }
}

@Composable
fun ProgressBar(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    audio : Song
) {
    Slider(
        value = progress,
        onValueChange = { onProgressChange.invoke(it) },
        valueRange = 0f..100f,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = timeStampToDuration(progress.toLong()) + " / ",
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = timeStampToDuration(audio.duration.toLong()), fontWeight = FontWeight.Light,
        )
    }
}

@Composable
fun PlayerControls(
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    skipNext : () -> Unit,
    skipPrevious : () -> Unit,
    shuffle: () -> Unit,
    repeat: (Int) -> Unit
) {
    var selected by remember {
       mutableStateOf(false)
    }

    var number by remember{
        mutableStateOf(1)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        IconButton(onClick = {
            shuffle.invoke()
            selected = !selected
        }) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "shuffle",
                tint = if(!selected) {
                    MaterialTheme.colors.onSurface.copy(alpha = .5f)
                }
                else{
                    MaterialTheme.colors.onSurface
                    },

                )
        }
        IconButton(onClick = {
            skipPrevious.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "SkipPrevious",
                modifier = Modifier.size(45.dp)
            )
        }
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(60.dp)
        ) {
            onStart.invoke()
        }
        IconButton(onClick = {
            skipNext.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "SkipNext",
                modifier = Modifier.size(45.dp)
            )
        }

        IconButton(onClick = {
            number = when(number){
                1->2
                2->3
                else->1
            }
            repeat.invoke(number)
        }) {
            Icon(
                imageVector = when(number){
                    1->Icons.Default.Repeat
                    2->Icons.Default.Repeat
                    3->Icons.Default.RepeatOne
                    else -> Icons.Default.RepeatOne
                },
                contentDescription = "Repeat",
                tint = if(number == 1)MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f)
                else MaterialTheme.colors
                    .onSurface,
            )
        }
    }
}

@Composable
fun MoreOptions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = CenterVertically
    ){
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "favorite",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
        }

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = "GraphicEq",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.PlaylistAdd,
                contentDescription = "add to playlist",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
        }

    }
}

