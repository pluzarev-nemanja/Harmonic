package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumDetailScreen
import com.example.mymusic.presentation.album.AlbumScreen
import com.example.mymusic.presentation.album.AlbumViewModel
import com.example.mymusic.presentation.history.HistoryScreen
import com.example.mymusic.presentation.history.HistoryViewModel
import com.example.mymusic.presentation.home.HomeScreen
import com.example.mymusic.presentation.playlist.PlaylistDetailsScreen
import com.example.mymusic.presentation.playlist.PlaylistScreen
import com.example.mymusic.presentation.playlist.PlaylistViewModel
import com.example.mymusic.presentation.search.SearchScreen
import com.example.mymusic.presentation.search.SearchViewModel
import com.example.mymusic.presentation.songs.SongsScreen
import com.example.mymusic.presentation.songs.SongsViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    songsViewModel: SongsViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    albumsViewModel: AlbumViewModel = hiltViewModel(),
    songList: List<Song>,
    searchText: String,
    songs: List<Song>,
    currentPlayingAudio: Song?,
    onItemClick: (Song) -> Unit,
    playlistViewModel: PlaylistViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(
                navController = navController,
                deleteAlbum = {
                    albumsViewModel.deleteAlbum(it)
                },
                albumsViewModel,
                currentPlayingAudio,
                shuffle = {
                    songsViewModel.shuffleSongs()
                },
            )
        }
        composable(BottomBarScreen.Songs.route) {
            SongsScreen(
                isAudioPlaying = songsViewModel.isAudioPlaying,
                audioList = songList,
                currentPlayingAudio = songsViewModel
                    .currentPlayingAudio.value,
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                sortOrderChange = {
                    songsViewModel.changeSortOrderSongs(it)
                },
                navController = navController,
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                }
            )
        }
        composable(BottomBarScreen.Playlists.route) {
            PlaylistScreen(
                playlistViewModel = playlistViewModel,
                sortOrderChange = {
                    playlistViewModel.changeSortOrder(it)
                },
                navController = navController,
                currentPlayingAudio = songsViewModel
                    .currentPlayingAudio.value,
                deletePlaylist = {
                    playlistViewModel.deletePlaylist(it)
                }
            )
        }
        composable(BottomBarScreen.Album.route) {
            AlbumScreen(
                currentPlayingAudio = currentPlayingAudio,
                navController = navController,
                albums = albumsViewModel.albums,
                sortOrderChange = {
                    albumsViewModel.changeSortOrder(it)
                },
                deleteAlbum = {
                    albumsViewModel.deleteAlbum(it)
                },
                albumsViewModel
            )
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(
                searchText,
                songs,
                searchViewModel,
                currentPlayingAudio,
                onItemClick={
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                }
            )
        }
        composable(Screen.PlaylistDetailsScreen.route) {

            PlaylistDetailsScreen(
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                navController = navController,
                playlist = playlistViewModel.clickedPlaylist.value,
                allPlaylists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                },
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                shuffle = { playlist ->
                    songsViewModel.shufflePlaylist(playlist = playlist)
                },
                onStart = { currentPlayingAudio, songs ->
                    songsViewModel.playPlaylist(currentPlayingAudio, songs)
                },
            )
        }

        composable(Screen.AlbumDetailScreen.route) {
            AlbumDetailScreen(
                navController = navController,
                album = albumsViewModel.albumNavigated.value,
                allPlaylists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                },
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                shuffle = { album ->
                    songsViewModel.shuffleAlbum(album)
                },
                onStart = { currentPlayingAudio, songs ->
                    songsViewModel.playPlaylist(currentPlayingAudio, songs)
                },

                )
        }

        composable(Screen.HistoryScreen.route){
            HistoryScreen(
                history = historyViewModel.history,
                navController = navController,
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                onItemClick = {
                    songsViewModel.playAudio(it)
                },
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                }
            )
        }

    }
}