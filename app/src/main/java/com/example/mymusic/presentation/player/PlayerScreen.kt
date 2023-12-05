package com.example.mymusic.presentation.player

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.R
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.songs.PlayerIconItem
import com.example.mymusic.presentation.songs.PlaylistChooser
import com.example.mymusic.presentation.songs.timeStampToDuration
import com.example.mymusic.presentation.util.shadow
import com.example.mymusic.presentation.util.snowfall
import com.example.mymusic.ui.theme.darkGreyToSoftGrey
import com.example.mymusic.ui.theme.darkestBlueToWhite
import com.example.mymusic.ui.theme.lightBlueToWhite
import com.example.mymusic.ui.theme.whiteToDarkGrey
import com.example.mymusic.ui.theme.whiteToDarkestBlue
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PlayerScreen(
    songName: String,
    artist: String,
    close: () -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    audio: Song,
    isAudioPlaying: Boolean,
    onStart: (Song) -> Unit,
    skipNext: () -> Unit,
    skipPrevious: () -> Unit,
    shuffle: () -> Unit,
    repeat: (Int) -> Unit,
    updateTimer: () -> String,
    addFavorite: (Song) -> Unit,
    isSelected: Boolean,
    isSnowing: Boolean,
    playlists: List<Playlist> = emptyList(),
    insertSongIntoPlaylist: (Song, String, String) -> Unit,
    shareSong: (Song) -> Unit,
    changeSongImage: (Song, String) -> Unit,
) {

    Surface(
        modifier = if (isSnowing) Modifier
            .background(MaterialTheme.colors.background)
            .snowfall()
        else Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                close,
                audio = audio,
                playlists = playlists,
                insertSongIntoPlaylist = insertSongIntoPlaylist,
                shareSong = shareSong,
                changeSongImage = changeSongImage
            )
            Spacer(modifier = Modifier.height(18.dp))
            GlideImage(
                imageModel = { if (audio.artUri != "") Uri.parse(audio.artUri) else R.drawable.note },
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
            SongInfo(
                songName,
                artist,
                addFavorite = addFavorite,
                song = audio,
                isSelected
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                progress = progress,
                onProgressChange = onProgressChange,
                audio = audio,
                updateTimer = { updateTimer.invoke() }
            )
            Spacer(modifier = Modifier.height(38.dp))
            PlayerControls(
                isAudioPlaying = isAudioPlaying,
                onStart = { onStart.invoke(audio) },
                skipNext = { skipNext.invoke() },
                skipPrevious = { skipPrevious.invoke() },
                shuffle = { shuffle.invoke() },
                repeat = repeat,

                )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}


@Composable
fun Header(
    close: () -> Unit,
    playlists: List<Playlist> = emptyList(),
    insertSongIntoPlaylist: (Song, String, String) -> Unit,
    shareSong: (Song) -> Unit,
    changeSongImage: (Song, String) -> Unit,
    audio: Song
    ) {

    var showMenu by remember { mutableStateOf(false) }
    var openDialog by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            //here update that in database user image
            changeSongImage.invoke(audio, uri.toString())
        }
    )

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
                tint = MaterialTheme.colors.lightBlueToWhite
            )
        }
        Text(
            modifier = Modifier
                .align(CenterVertically),
            text = "Now Playing",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        IconButton(onClick = {
            showMenu = true
        }) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More options",
                tint = MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f),

                )
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = {
                        showMenu = false
                    },
                ) {
                    DropdownMenuItem(onClick = {
                        openDialog = true
                        showMenu = false
                    }) {
                        Text(
                            text = "Add song to playlist",

                            )
                    }
                    DropdownMenuItem(onClick = {
                        shareSong.invoke(audio)
                        showMenu = false
                    }) {
                        Text(
                            text = "Share song",
                        )
                    }
                    DropdownMenuItem(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        showMenu = false
                    }) {
                        Text(text = "Change image")
                    }
                }
            }
            if (openDialog) {

                var selectedIndex by remember {
                    mutableStateOf("")
                }
                var image by remember {
                    mutableStateOf("")
                }

                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp),
                    shape = RoundedCornerShape(10.dp),
                    onDismissRequest = {
                        openDialog = false
                    },
                    title = {
                        Text(
                            text = "Choose playlist",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    },
                    text = {
                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 12.dp)
                        ) {
                            items(playlists) { playlist ->
                                PlaylistChooser(playlist = playlist,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = selectedIndex == playlist.playlistName,
                                            onClick = {
                                                selectedIndex =
                                                    if (selectedIndex == playlist.playlistName) "" else playlist.playlistName
                                                image =
                                                    if (selectedIndex == playlist.playlistName) playlist.playlistImage else ""
                                            }
                                        )
                                        .background(
                                            if (selectedIndex == playlist.playlistName) Color.Gray
                                            else Color.Transparent
                                        )
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                insertSongIntoPlaylist.invoke(audio, selectedIndex, image)
                                openDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.lightBlueToWhite)
                        ) {
                            Text(
                                "Add",
                                color = MaterialTheme.colors.whiteToDarkGrey
                            )
                        }
                    },
                    dismissButton = {

                        Button(
                            onClick = {
                                openDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SongInfo(
    songName: String,
    artist: String,
    addFavorite: (Song) -> Unit,
    song: Song,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            IconButton(onClick = {
                addFavorite.invoke(song)
            }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "favorite",
                    tint = if (isSelected) Color.Red else Color.Gray
                )
            }
            Text(
                text = songName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    audio: Song,
    updateTimer: () -> String
) {

    var currentPos = progress //original variable
    val currentPosTemp = remember { mutableStateOf(0f) } //temporary variable
    Slider(
        value = if (currentPosTemp.value == 0f) currentPos else currentPosTemp.value,
        onValueChange = {
            currentPosTemp.value = it
            onProgressChange.invoke(it)
        },
        onValueChangeFinished = {
            currentPos = currentPosTemp.value
            currentPosTemp.value = 0f
            //send the currentPos value for updating the Slider progress
        },
        valueRange = 0f..100f,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.darkestBlueToWhite,
            activeTrackColor = MaterialTheme.colors.darkGreyToSoftGrey,
            inactiveTrackColor = MaterialTheme.colors.lightBlueToWhite,
        ),
        steps = 0,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = updateTimer.invoke() + " / ",
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
    skipNext: () -> Unit,
    skipPrevious: () -> Unit,
    shuffle: () -> Unit,
    repeat: (Int) -> Unit,
) {
    var selected by remember {
        mutableStateOf(false)
    }

    var number by remember {
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
                tint = if (!selected) {
                    MaterialTheme.colors.onSurface.copy(alpha = .5f)
                } else {
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
                modifier = Modifier.size(55.dp)
            )
        }
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow,
            backgroundColor = MaterialTheme.colors.darkestBlueToWhite,
            modifier = Modifier
                .size(70.dp),
            color = MaterialTheme.colors.whiteToDarkestBlue
        ) {
            onStart.invoke()
        }
        IconButton(onClick = {
            skipNext.invoke()
        }) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "SkipNext",
                modifier = Modifier.size(55.dp)
            )
        }

        IconButton(onClick = {
            number = when (number) {
                1 -> 2
                2 -> 3
                else -> 1
            }
            repeat.invoke(number)
        }) {
            Icon(
                imageVector = when (number) {
                    1 -> Icons.Default.Repeat
                    2 -> Icons.Default.Repeat
                    3 -> Icons.Default.RepeatOne
                    else -> Icons.Default.RepeatOne
                },
                contentDescription = "Repeat",
                tint = if (number == 1) MaterialTheme.colors
                    .onSurface
                    .copy(alpha = .5f)
                else MaterialTheme.colors
                    .onSurface,
            )
        }
    }
}