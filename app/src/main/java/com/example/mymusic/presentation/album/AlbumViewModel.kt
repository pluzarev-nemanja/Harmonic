package com.example.mymusic.presentation.album

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.use_cases.MusicUseCases
import com.example.mymusic.domain.util.AlbumSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases
) : ViewModel() {

    var albums = mutableStateListOf<Album>()

    var albumNavigated = mutableStateOf<Album?>(null)
        private set

    init {
        viewModelScope.launch {
            musicUseCases.insertAlbum()
        }
        viewModelScope.launch {
            musicUseCases.getAllAlbumsAsc().collect { album ->
                albums.clear()
                albums += album
            }
        }
    }

    fun addAlbum(album: Album) {
        albumNavigated.value = album
        viewModelScope.launch {
            musicUseCases.getAlbumWithSongs(album.albumName).collect { albumsWithSongs ->
                albumNavigated.value!!.songs = albumsWithSongs[0].songs
            }
        }
    }


    fun changeSortOrder(sortOrder: AlbumSortOrder) {
        when (sortOrder) {
            AlbumSortOrder.ALBUM_NAME -> {
                viewModelScope.launch {
                    musicUseCases.getAllAlbumsAsc().collect { album ->
                        albums.clear()
                        albums += album
                    }
                }
            }

            AlbumSortOrder.ARTIST -> {
                viewModelScope.launch {
                    musicUseCases.getAllAlbumsArtist().collect { album ->
                        albums.clear()
                        albums += album

                    }
                }
            }

            AlbumSortOrder.SONG_COUNT -> {
                viewModelScope.launch {
                    musicUseCases.getAllAlbumsSongCount().collect { album ->
                        albums.clear()
                        albums += album

                    }
                }
            }
        }
    }

    fun changeAlbumImage(album: Album, albumImage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.updateAlbum(
                album.copy(
                    albumImage = albumImage
                )
            )
        }
    }

}