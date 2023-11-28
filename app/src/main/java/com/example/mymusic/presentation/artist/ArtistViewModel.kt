package com.example.mymusic.presentation.artist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel(){

    var artists = mutableStateListOf<Artist>()

    var artistNavigated = mutableStateOf<Artist?>(null)
        private set
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
    fun changeArtistImage(artist: Artist, artistImage : String){
        viewModelScope.launch(Dispatchers.IO){
            musicUseCases.updateArtist(
                artist.copy(
                    artistImage = artistImage
                )
            )
        }
    }

    fun addArtist(artist: Artist){
        artistNavigated.value = artist
        viewModelScope.launch {
            musicUseCases.getArtistWithSongs(artist.artist).collect{ artistWithSongs->
                artistNavigated.value!!.songs = artistWithSongs[0].songs
            }
        }
    }

}