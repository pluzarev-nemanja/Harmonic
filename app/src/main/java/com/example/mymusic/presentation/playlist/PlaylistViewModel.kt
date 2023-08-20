package com.example.mymusic.presentation.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.use_cases.MusicUseCases
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

    fun insertPlaylist(playlistName: String){
        playlists.clear()
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
            musicUseCases.insertPlaylist(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch {
            musicUseCases.deletePlaylist(playlist)
        }
    }

}
