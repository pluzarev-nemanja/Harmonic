package com.example.mymusic.presentation.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymusic.R
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumItem
import com.example.mymusic.presentation.album.AlbumViewModel
import com.example.mymusic.presentation.navigation.Screen
import com.example.mymusic.presentation.songs.TOP_BAR_HEIGHT
import com.example.mymusic.presentation.songs.isScrolled
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(
    navController: NavController,
    deleteAlbum: (Album) -> Unit,
    albumViewModel: AlbumViewModel,
    currentPlayingAudio: Song?,
    shuffle : () -> Unit
    ) {

    val lazyState = rememberLazyListState()

    val paddingLazyList by animateDpAsState(
        targetValue = if (lazyState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingLazyList)
                .verticalScroll(rememberScrollState())
        ) {
            UserInfo()
            Spacer(modifier = Modifier.height(8.dp))
            Buttons(navController,shuffle)
            Suggestions()
            Spacer(modifier = Modifier.height(8.dp))
            RecentAlbums(
                albums = albumViewModel.albums,
                deleteAlbum = deleteAlbum,
                albumViewModel,
                navController
            )
            Spacer(modifier = Modifier.height(8.dp))
            TopArtist(
                albums = albumViewModel.albums,
                deleteAlbum = deleteAlbum,
                albumViewModel,
                navController,
                modifier = Modifier.padding(bottom = animatedHeight)
            )
        }

        HomeTopBar(
            lazyListState = lazyState,
            navController = navController
        )
    }
}


@Composable
fun UserInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier
                .padding(5.dp)
                .size(70.dp)
                .clip(CircleShape),
            imageModel = { R.mipmap.ic_launcher },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(
                text = "Welcome!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "User Name",
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun Suggestions() {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Suggestions")
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        ) {
            items(5) {
                Box(modifier = Modifier
                    .size(70.dp)
                    .background(Color.Magenta))
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun RecentAlbums(
    albums: List<Album>,
    deleteAlbum: (Album) -> Unit,
    albumViewModel: AlbumViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "All Albums")
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "open")
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        ) {
            items(albums) { album ->
                AlbumItem(
                    album = album,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    deleteAlbum = deleteAlbum,
                    navController,
                    albumViewModel = albumViewModel
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(
    lazyListState: LazyListState,
    navController: NavController
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .animateContentSize(animationSpec = tween(durationMillis = 300))
            .height(height = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT),

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                //navigate to search screen
                navController.navigate(Screen.SearchScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search button")
            }
            Text(
                text = "MY music",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    color = Color.Red,
                    fontStyle = FontStyle.Italic
                )

            )

            IconButton(onClick = {
                //navigate to settings Screen
            }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings button")
            }
        }
    }
}

@Composable
fun Buttons(
    navController: NavController,
    shuffle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                      navController.navigate(Screen.HistoryScreen.route)
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(60.dp)
        ) {
            Icon(imageVector = Icons.Filled.History, contentDescription = "history")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "History")
        }
        Button(
            onClick = { shuffle.invoke() },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(60.dp)
        ) {
            Icon(imageVector = Icons.Filled.Shuffle, contentDescription = "shuffle")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Shuffle")
        }
        Button(
            onClick = { navController.navigate(Screen.FavoriteScreen.route)},
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(60.dp)
        ) {
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "favorite")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Favorite")
        }
    }
}

@Composable
fun TopArtist(
    albums: List<Album>,
    deleteAlbum: (Album) -> Unit,
    albumViewModel: AlbumViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "All Artists")
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "open")
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        ) {
            items(albums) { album ->
                AlbumItem(
                    album = album,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    deleteAlbum = deleteAlbum,
                    navController,
                    albumViewModel = albumViewModel
                )
            }
        }
    }
}