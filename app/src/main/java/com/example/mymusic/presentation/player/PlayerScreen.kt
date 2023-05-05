package com.example.mymusic.presentation.player

import android.net.Uri
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.mymusic.presentation.util.Marquee
import com.example.mymusic.presentation.util.defaultMarqueeParams
import com.example.mymusic.presentation.util.shadow

@Composable
fun PlayerScreen(
    image : String,
    songName : String,
    artist : String,
    close : () -> Unit
) {

    Surface(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(close)
            Spacer(modifier = Modifier.height(18.dp))
            Image(
                imageVector = Icons.Default.Headphones,
                contentDescription = "Song Image",
                modifier = Modifier
                    .shadow(offsetY = 230.dp, blurRadius = 15.dp)
                    .size(300.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.DarkGray),
            )
            Spacer(modifier = Modifier.height(38.dp))
            SongInfo(songName, artist)
            Spacer(modifier = Modifier.height(38.dp))
            ProgressBar()
            Spacer(modifier = Modifier.height(38.dp))
            PlayerControls()
            Spacer(modifier = Modifier.height(18.dp))
            MoreOptions()
        }
    }
}
@Composable
fun PlayButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    border: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colors.primary,
    color: Color = MaterialTheme.colors.onSurface,
    onClick: () -> Unit
) {

    Surface(
        shape = CircleShape,
        border = border,
        modifier = modifier
            .clickable {
                onClick.invoke()
            },
        contentColor = color,
        color = backgroundColor

    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                Modifier.size(30.dp)
            )

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Marquee(
            params = defaultMarqueeParams(),
        ) {
                Text(
                    text = songName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
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
fun ProgressBar() {
    LinearProgressIndicator()
    Spacer(modifier = Modifier.height(18.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "1:04 / ",
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "2:23", fontWeight = FontWeight.Light,
        )
    }
}

@Composable
fun PlayerControls() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "shuffle",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "SkipPrevious",
                modifier = Modifier.size(45.dp)
            )
        }

        PlayButton(
            icon = Icons.Default.Pause,
            modifier = Modifier.size(75.dp)

        ) {

        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "SkipNext",
                modifier = Modifier.size(45.dp)
            )
        }

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),
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

