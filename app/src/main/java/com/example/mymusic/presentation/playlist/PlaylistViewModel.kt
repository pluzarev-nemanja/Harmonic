package com.example.mymusic.presentation.playlist

import androidx.compose.runtime.mutableStateListOf
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

    init {
        viewModelScope.launch {
            musicUseCases.getAllPlaylists().collect{ playlist ->
                playlists += playlist
            }
        }
    }


    fun insertSongIntoPlaylist(song: Song,playlistName: String){
        viewModelScope.launch {

        }
    }
    fun insertPlaylist(playlistName: String){
        if (playlistName.isBlank()) {
            return
        }
        val playlist = Playlist(
            playlistName = playlistName,
            songCount = 0,
            playlistDuration = 0L,
            songs = emptyList()
        )
        viewModelScope.launch {
            playlists.clear()
            musicUseCases.insertPlaylist(playlist)
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
