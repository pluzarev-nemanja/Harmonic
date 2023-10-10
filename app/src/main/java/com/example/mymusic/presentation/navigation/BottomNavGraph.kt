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
import com.example.mymusic.presentation.artist.AllArtistsScreen
import com.example.mymusic.presentation.favorite.FavoriteScreen
import com.example.mymusic.presentation.favorite.FavoriteViewModel
import com.example.mymusic.presentation.history.HistoryScreen
import com.example.mymusic.presentation.history.HistoryViewModel
import com.example.mymusic.presentation.home.AllAlbumsScreen
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
    playlistViewModel: PlaylistViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
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
                currentPlayingAudio = currentPlayingAudio,
                shuffle = {
                    songsViewModel.shuffleSongs()
                },
                refreshSuggestions = {
                    songsViewModel.suggestions()
                },
                suggestions = songsViewModel.suggestions,
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                albums = albumsViewModel.albums,
                addAlbum = {
                    albumsViewModel.addAlbum(it)
                }
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
                sortOrderChange = {
                    playlistViewModel.changeSortOrder(it)
                },
                navController = navController,
                currentPlayingAudio = songsViewModel
                    .currentPlayingAudio.value,
                deletePlaylist = {
                    playlistViewModel.deletePlaylist(it)
                },
                playlists = playlistViewModel.playlists,
                addPlaylist = {
                    playlistViewModel.addPlaylist(it)
                },
                insertPlaylist = {
                    playlistViewModel.insertPlaylist(it)
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
                addAlbum = {
                    albumsViewModel.addAlbum(it)
                }
            )
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(
                searchText,
                songs,
                currentPlayingAudio,
                onItemClick={
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                },
                onSearchTextChange = {
                    searchViewModel.onSearchTextChange(it)
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

        composable(Screen.FavoriteScreen.route){
            FavoriteScreen(
                navController = navController,
                favorite = favoriteViewModel.songList,
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                }
            )
        }

        composable(Screen.AllAlbumsScreen.route){
            AllAlbumsScreen(
                albums = albumsViewModel.albums,
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                deleteAlbum = {
                    albumsViewModel.deleteAlbum(it)
                },
                addAlbum = {
                    albumsViewModel.addAlbum(it)
                },
                navController = navController
            )
        }

        composable(Screen.AllArtistsScreen.route){
            AllArtistsScreen()
        }

    }
}