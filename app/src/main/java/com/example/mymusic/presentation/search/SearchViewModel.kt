package com.example.mymusic.presentation.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    private var searchList = mutableStateListOf<Song>()
    private var searchAlbum = mutableStateListOf<Album>()
    private var searchArtist = mutableStateListOf<Artist>()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)


    private var _songs = MutableStateFlow(searchList)
    private var _albums = MutableStateFlow(searchAlbum)
    private var _artist = MutableStateFlow(searchArtist)

    val artist = searchText
        .onEach { _isSearching.update { true } }
        .combine(_artist) { text, artist ->
            if (text.isBlank()) {
                artist
            } else {
                artist.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _artist.value
        )

    val album = searchText
        .onEach { _isSearching.update { true } }
        .combine(_albums) { text, album ->
            if (text.isBlank()) {
                album
            } else {
                album.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _albums.value
        )

    val songs = searchText
        .onEach { _isSearching.update { true } }
        .combine(_songs) { text, song ->
            if (text.isBlank()) {
                song
            } else {
                song.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _songs.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    init {
        viewModelScope.launch {
            musicUseCases.getAllSongsAsc().collect { songsList ->
                searchList.clear()
                searchList += songsList.map {
                    val displayName = it.displayName.substringBefore(".")
                    val artist = if (it.artist.contains("<unknown>"))
                        "Unknown Artist" else it.artist

                    it.copy(
                        displayName = displayName,
                        artist = artist
                    )
                }
            }
        }
        viewModelScope.launch {
            musicUseCases.getArtists().collect { artists ->
                searchArtist.clear()
                searchArtist += artists
            }
        }
        viewModelScope.launch {
            musicUseCases.getAllAlbumsAsc().collect { album ->
                searchAlbum.clear()
                searchAlbum += album
            }
        }
    }
}