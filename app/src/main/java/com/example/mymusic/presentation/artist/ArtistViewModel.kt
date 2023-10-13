package com.example.mymusic.presentation.artist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel(){

    var artists = mutableStateListOf<Artist>()


    init {

        viewModelScope.launch {
            musicUseCases.insertArtists()
        }
        viewModelScope.launch {
            musicUseCases.getArtists().collect{ artist->
                artists.clear()
                artists += artist
            }
        }
    }
}