package com.example.mymusic.presentation.songs

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import com.example.mymusic.domain.util.SortOrder
import com.example.mymusic.presentation.exoplayer.MediaPlayerServiceConnection
import com.example.mymusic.presentation.exoplayer.currentPosition
import com.example.mymusic.presentation.exoplayer.isPlaying
import com.example.mymusic.presentation.service.MusicPlayerService
import com.example.mymusic.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {


    var songList = mutableStateListOf<Song>()


    val currentPlayingAudio = serviceConnection.currentPlayingAudio
    private val isConnected = serviceConnection.isConnected
    private lateinit var rootMediaId: String
    private var currentPlayBackPosition by mutableStateOf(0L)
    private var updatePosition = true
    private val playbackState = serviceConnection.plaBackState
    val isAudioPlaying: Boolean
        get() = playbackState.value?.isPlaying == true
    private val subscriptionCallback = object
        : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            super.onChildrenLoaded(parentId, children)
        }
    }
    private val serviceConnection = serviceConnection.also {
        updatePlayBack()
    }

    private val currentDuration: Long
        get() = MusicPlayerService.currentDuration

    var currentAudioProgress = mutableStateOf(0f)


    init {
        viewModelScope.launch {
            musicUseCases.insertSong()
        }
        viewModelScope.launch {
            musicUseCases.getAllSongsAsc().collect { songsList ->
                songList += songsList.map {
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
            isConnected.collect {
                if (it) {
                    rootMediaId = serviceConnection.rootMediaId
                    serviceConnection.plaBackState.value?.apply {
                        currentPlayBackPosition = position
                    }
                    serviceConnection.subscribe(rootMediaId, subscriptionCallback)
                }
            }
        }
    }

    fun changeSortOrderSongs(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsAsc().collect{ songsList ->
                        songList.clear()
                        songList += songsList.map {
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
            SortOrder.DESCENDING -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsDesc().collect{ songsList ->
                        songList.clear()
                        songList += songsList.map {
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
            SortOrder.ARTIST -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsArtist().collect{ songsList ->
                        songList.clear()
                        songList += songsList.map {
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
            SortOrder.ALBUM -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsAlbum().collect{ songsList ->
                        songList.clear()
                        songList += songsList.map {
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
            SortOrder.DATE_ADDED -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsDate().collect{ songsList ->
                        songList.clear()
                        songList += songsList.map {
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
    }

    fun playAudio(currentAudio: Song) {
        serviceConnection.playAudio(songList)
        if (currentAudio.id == currentPlayingAudio.value?.id) {
            if (isAudioPlaying) {
                serviceConnection.transportControl.pause()
            } else {
                serviceConnection.transportControl.play()
            }


        } else {
            serviceConnection.transportControl
                .playFromMediaId(
                    currentAudio.id.toString(),
                    null
                )
        }
    }

    fun stopPlayBack() {
        serviceConnection.transportControl.stop()
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    fun skipToNext() {
        serviceConnection.skipToNext()
    }

    fun seekTo(value: Float) {
        serviceConnection.transportControl.seekTo(
            (currentDuration * value / 100f).toLong()
        )
    }

    private fun updatePlayBack() {
        viewModelScope.launch {
            val position = playbackState.value?.currentPosition ?: 0

            if (currentPlayBackPosition != position) {
                currentPlayBackPosition = position
            }

            if (currentDuration > 0) {
                currentAudioProgress.value = (
                        currentPlayBackPosition.toFloat()
                                / currentDuration.toFloat() * 100f

                        )
            }

            delay(Constants.PLAYBACK_UPDATE_INTERVAL)
            if (updatePosition) {
                updatePlayBack()
            }


        }


    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unSubscribe(
            Constants.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
        updatePosition = false
    }
}

