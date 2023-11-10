package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymusic.domain.model.Song
import com.example.mymusic.presentation.album.AlbumDetailScreen
import com.example.mymusic.presentation.album.AlbumScreen
import com.example.mymusic.presentation.album.AlbumViewModel
import com.example.mymusic.presentation.artist.AllArtistsScreen
import com.example.mymusic.presentation.artist.ArtistViewModel
import com.example.mymusic.presentation.artist.ArtistsDetailScreen
import com.example.mymusic.presentation.favorite.FavoriteScreen
import com.example.mymusic.presentation.favorite.FavoriteViewModel
import com.example.mymusic.presentation.history.HistoryScreen
import com.example.mymusic.presentation.history.HistoryViewModel
import com.example.mymusic.presentation.home.AllAlbumsScreen
import com.example.mymusic.presentation.home.HomeScreen
import com.example.mymusic.presentation.main.MainViewModel
import com.example.mymusic.presentation.playlist.PlaylistDetailsScreen
import com.example.mymusic.presentation.playlist.PlaylistScreen
import com.example.mymusic.presentation.playlist.PlaylistViewModel
import com.example.mymusic.presentation.search.SearchScreen
import com.example.mymusic.presentation.search.SearchViewModel
import com.example.mymusic.presentation.settings.SettingsScreen
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
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    artistViewModel: ArtistViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = viewModel(),
    equalizer: () -> Unit,
    shareSong: (Song) -> Unit
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
                },
                artists = artistViewModel.artists,
                addArtist = {
                    artistViewModel.addArtist(it)
                }
            )
        }
        composable(BottomBarScreen.Songs.route) {
            SongsScreen(
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
                },
                shareSong = shareSong
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
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                playlists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                },
                onSearchTextChange = {
                    searchViewModel.onSearchTextChange(it)
                },
                shareSong = shareSong
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
                shareSong = shareSong
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
                shareSong
            )
        }

        composable(Screen.HistoryScreen.route) {
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
                },
                shareSong = shareSong
            )
        }

        composable(Screen.FavoriteScreen.route) {
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
                },
                shareSong
            )
        }

        composable(Screen.AllAlbumsScreen.route) {
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

        composable(Screen.AllArtistsScreen.route) {
            AllArtistsScreen(
                navController = navController,
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                artists = artistViewModel.artists,
                addArtist = {
                    artistViewModel.addArtist(it)
                }
            )
        }

        composable(Screen.ArtistDetailScreen.route) {
            ArtistsDetailScreen(
                navController = navController,
                artist = artistViewModel.artistNavigated.value,
                allPlaylists = playlistViewModel.playlists,
                insertSongIntoPlaylist = { song, playlistName ->
                    playlistViewModel.insertSongIntoPlaylist(song, playlistName)
                },
                onItemClick = {
                    songsViewModel.playAudio(it)
                    historyViewModel.updateHistory(it)
                },
                currentPlayingAudio = songsViewModel.currentPlayingAudio.value,
                shuffle = { artist ->
                    songsViewModel.shuffleArtist(artist)
                },
                onStart = { currentPlayingAudio, songs ->
                    songsViewModel.playPlaylist(currentPlayingAudio, songs)
                },
                shareSong = shareSong
            )
        }

        composable(Screen.SettingsScreen.route) {
            SettingsScreen(
                navController = navController,
                userName = "User Name",
                onThemeChange = {
                    mainViewModel.onThemeChanged(it)
                },
                mainViewModel,
                equalizer = equalizer
            )
        }
    }
}