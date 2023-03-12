package com.example.mymusic.presentation.search

import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel(){

    var searchList = mutableStateListOf<Song>()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private var _songs = MutableStateFlow(searchList)
    val songs = searchText
        .onEach { _isSearching.update { true } }
        .combine(_songs) { text, song ->
            if(text.isBlank()) {
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
    }
}