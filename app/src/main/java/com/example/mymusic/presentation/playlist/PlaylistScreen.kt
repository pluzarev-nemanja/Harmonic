package com.example.mymusic.presentation.playlist

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.R
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.presentation.songs.timeStampToDuration
import com.example.mymusic.presentation.util.shadow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    playlistViewModel: PlaylistViewModel
) {

    var openDialog by remember {
        mutableStateOf(false)
    }
    var filledText by remember {
        mutableStateOf("")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openDialog = true

            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new playlist")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = playlistViewModel.playlists
            ) { playlist: Playlist ->
                PlaylistItem(playlist = playlist)
            }
        }
        if (openDialog) {
            AlertDialog(
                shape = RoundedCornerShape(10.dp),
                onDismissRequest = {
                    openDialog = false
                },
                title = {
                    Text(
                        text = "New Playlist"
                    )
                },
                text = {
                    OutlinedTextField(
                        value = filledText,
                        onValueChange = {
                            filledText = it
                        },
                        label = {
                            Text(text = "Enter playlist name")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog = false
                            playlistViewModel.insertPlaylist(filledText)
                        }) {
                        Text("Create playlist")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog = false
                        }) {
                        Text("Cancel")
                    }
                    filledText = ""
                }
            )
        }
    }


}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GlideImage(
                imageModel = { R.mipmap.ic_launcher },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .shadow(offsetY = 130.dp, blurRadius = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.DarkGray),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = playlist.playlistName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = "More options")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${playlist.songCount} songs ")
                Text(text = "· ${timeStampToDuration(playlist.playlistDuration)}")
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

