package com.example.mymusic.presentation.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumItem
import com.example.mymusic.presentation.history.SimpleTopBar
import com.example.mymusic.presentation.songs.TOP_BAR_HEIGHT
import com.example.mymusic.presentation.songs.isScrolled

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllAlbumsScreen(
    albums: List<Album>,
    currentPlayingAudio: Song?,
    addAlbum: (Album) -> Unit,
    navController: NavController,
    changeAlbumImage : (Album,String) -> Unit

) {

    val scrollState = rememberLazyStaggeredGridState()

    val animatedHeight by animateDpAsState(
        targetValue = if (currentPlayingAudio == null) 0.dp
        else 80.dp, label = "animatedHeight"
    )
    val paddingLazyList by animateDpAsState(
        targetValue = if (scrollState.isScrolled) 0.dp else TOP_BAR_HEIGHT,
        animationSpec = tween(durationMillis = 300), label = "paddingLazyList"
    )

    Scaffold (
        topBar = {
            SimpleTopBar(navController, name = "All albums")
        }
    ){ padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingLazyList),
            contentPadding = PaddingValues(16.dp, bottom = animatedHeight),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState,
        ) {

            items(albums) { album ->
                AlbumItem(
                    album = album,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    navController,
                    addAlbum = addAlbum,
                    changeAlbumImage = changeAlbumImage
                )
            }
        }
    }
}