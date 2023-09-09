package com.example.mymusic.presentation.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import com.example.mymusic.domain.util.PlaylistSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var playlists = mutableStateListOf<Playlist>()

    var clickedPlaylist = mutableStateOf<Playlist?>(null)
    private set
    init {
        viewModelScope.launch {
            musicUseCases.getAllPlaylists().collect{ playlist ->
                playlists.clear()
                playlists += playlist
            }
        }
    }

    fun addPlaylist(playlist: Playlist){
        clickedPlaylist.value = playlist
    }
    fun insertSongIntoPlaylist(song: Song,playlistName: String){
        playlists.forEach { playlist: Playlist ->

            if(playlist.playlistName == playlistName && playlistName!= "" && !playlist.songs.contains(song)){

                viewModelScope.launch {
                    playlists.clear()
                    val songs = playlist.songs.toMutableList()
                    songs.add(song)
                    val item = Playlist(
                        playlistName = playlistName,
                        songCount = playlist.songCount + 1,
                        playlistDuration = playlist.playlistDuration + song.duration,
                        songs = songs
                    )
                    musicUseCases.updatePlaylist(item)
                }
            }
        }
    }
    fun insertPlaylist(playlistName: String){

    playlists.forEach { playlist: Playlist ->

        if (playlist.playlistName != playlistName && playlistName.isNotBlank()) {

                viewModelScope.launch {
                    playlists.clear()
                    val playlistItem = Playlist(
                        playlistName = playlistName,
                        songCount = 0,
                        playlistDuration = 0L,
                        songs = emptyList()
                    )
                    musicUseCases.insertPlaylist(playlistItem)
                }
            }
        }
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch {
            musicUseCases.deletePlaylist(playlist)
        }
    }

    fun changeSortOrder(sortOrder: PlaylistSortOrder){
        when(sortOrder){
            PlaylistSortOrder.ASCENDING -> {
                viewModelScope.launch {
                    musicUseCases.getAllPlaylists().collect{ playlist ->
                        playlists.clear()
                        playlists += playlist

                    }
                }
            }
            PlaylistSortOrder.DESCENDING -> {
                viewModelScope.launch {
                    musicUseCases.getAllPlaylistsDesc().collect{ playlist ->
                        playlists.clear()
                        playlists += playlist

                    }
                }
            }
            PlaylistSortOrder.SONG_COUNT -> {
                viewModelScope.launch {
                    musicUseCases.getAllPlaylistsSongCount().collect{ playlist ->
                        playlists.clear()
                        playlists += playlist

                    }
                }
            }
            PlaylistSortOrder.DURATION -> {
                viewModelScope.launch {
                    musicUseCases.getAllPlaylistsDuration().collect{ playlist ->
                        playlists.clear()
                        playlists += playlist
                    }
                }
            }
        }
    }

}
