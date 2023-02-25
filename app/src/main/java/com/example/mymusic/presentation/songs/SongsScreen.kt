package com.example.mymusic.presentation.songs

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.util.Marquee
import com.example.mymusic.presentation.util.defaultMarqueeParams
import com.example.mymusic.presentation.util.scrollbar
import kotlin.math.floor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongsScreen(
    isAudioPlaying: Boolean,
    audioList: List<Song>,
    currentPlayingAudio: Song?,
    onStart: (Song) -> Unit,
    onItemClick: (Song) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp
    )

    val scrollState = rememberLazyListState()



    BottomSheetScaffold(
        sheetContent = {
            currentPlayingAudio?.let { currentPlayingAudio ->
                BottomBarPlayer(
                    song = currentPlayingAudio,
                    isAudioPlaying = isAudioPlaying,
                    onStart = { onStart.invoke(currentPlayingAudio) }
                )

            }
        },
        sheetShape = RoundedCornerShape(topEnd = 26.dp, topStart = 26.dp),
        scaffoldState = scaffoldState,
        sheetPeekHeight = animatedHeight
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 56.dp),
            modifier = Modifier.scrollbar(
                scrollState,
                thickness = 10.dp,
                knobColor = Color.Cyan,
                trackColor = Color.LightGray,
                padding = 6.dp,
                fixedKnobRatio = 0.07f
            ),
            state = scrollState
        ) {
            items(audioList) { song: Song ->
                AudioItem(
                    audio = song,
                    onItemClick = { onItemClick.invoke(song) },
                )
            }
        }

    }


}

@Composable
fun AudioItem(
    audio: Song,
    onItemClick: (id: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClick.invoke(audio.id)
            },
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = .5f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            //here image

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = audio.displayName,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = audio.artist,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors
                            .onSurface
                            .copy(alpha = .5f)
                    )

                    Text(
                        text = " · " + timeStampToDuration(audio.duration.toLong()),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors
                            .onSurface
                            .copy(alpha = .5f)
                    )
                }
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "More options",
                    tint = MaterialTheme.colors
                        .onSurface
                        .copy(alpha = .5f),

                    )
            }
            Spacer(modifier = Modifier.size(8.dp))
        }

    }


}

private fun timeStampToDuration(position: Long): String {
    val totalSeconds = floor(position / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)

    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)


}


@Composable
fun BottomBarPlayer(
    song: Song,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArtistInfo(
                audio = song,
                modifier = Modifier
                    .weight(1f)
            )
            MediaPlayerController(
                isAudioPlaying = isAudioPlaying,
                onStart = { onStart.invoke() },
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
}

@Composable
fun MediaPlayerController(
    modifier: Modifier = Modifier,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(60.dp)
            .padding(1.dp)
    ) {
        PlayerIconItem(
            icon = Icons.Default.Favorite,
            color = Color.White,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 6.dp)
        ) {
            //on like button click
        }
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(50.dp)
        ) {
            onStart.invoke()
        }
    }
}


@Composable
fun ArtistInfo(
    modifier: Modifier = Modifier,
    audio: Song
) {
    Row(
        modifier = modifier.padding(start = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        PlayerIconItem(
            icon = Icons.Default.MusicNote,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface
            ),
            modifier = Modifier.size(60.dp)
        ) {}
        Spacer(modifier = Modifier.size(4.dp))

        Column{
            Marquee(
                modifier = Modifier,
                params = defaultMarqueeParams(),
            ) {
                Text(
                    text = audio.title,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = audio.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}


@Composable
fun PlayerIconItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    border: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colors.surface,
    color: Color = MaterialTheme.colors.onSurface,
    onClick: () -> Unit
) {

    Surface(
        shape = RoundedCornerShape(15.dp),
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
            )

        }


    }


}
