package com.example.mymusic.presentation.album

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel  @Inject constructor(
    private val musicUseCases: MusicUseCases
): ViewModel() {

    var albums = mutableStateListOf<Album>()

    init {
        viewModelScope.launch {
            musicUseCases.insertAlbum()
        }
        viewModelScope.launch {
            musicUseCases.getAllAlbumsAsc().collect{album->
                albums.clear()
                albums += album
            }
        }
    }
}