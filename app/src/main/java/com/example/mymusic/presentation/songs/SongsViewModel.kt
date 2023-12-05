package com.example.mymusic.presentation.songs

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import com.example.mymusic.domain.util.SortOrder
import com.example.mymusic.presentation.exoplayer.MediaPlayerServiceConnection
import com.example.mymusic.presentation.exoplayer.currentPosition
import com.example.mymusic.presentation.exoplayer.isPlaying
import com.example.mymusic.presentation.service.MusicPlayerService
import com.example.mymusic.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor
import kotlin.random.Random

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {


    var songList = mutableStateListOf<Song>()
    var suggestions = mutableStateListOf<Song>()

    private var timer by mutableStateOf("")
    private var shuffleMode by mutableStateOf(false)
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
                suggestions()
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

    fun stockEqualizer(activity: Activity) {
        val sessionId = 1
        try {
            val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
            effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            activity.startActivityForResult(effects, 0)
        } catch (notFound: ActivityNotFoundException) {
            Log.d("EQ", "No equalizer found")
        }
    }

    fun suggestions() {
        suggestions.clear()
        if (songList.size != 0) {
            if (songList.size >= 5) {
                for (i in 0..5) {
                    val number = Random.nextInt(from = 0, until = songList.size)
                    suggestions.add(i, songList[number])
                }
            } else {
                for (i in 0 until songList.size) {
                    val number = Random.nextInt(from = 0, until = songList.size)
                    suggestions.add(i, songList[number])
                }
            }
        }
    }

    fun changeSongImage(song: Song, songImage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            musicUseCases.updateSong(
                song.copy(
                    artUri = songImage
                )
            )
        }
    }

    fun changeSortOrderSongs(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                viewModelScope.launch {
                    musicUseCases.getAllSongsAsc().collect { songsList ->
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
                    musicUseCases.getAllSongsDesc().collect { songsList ->
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
                    musicUseCases.getAllSongsArtist().collect { songsList ->
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
                    musicUseCases.getAllSongsAlbum().collect { songsList ->
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
                    musicUseCases.getAllSongsDate().collect { songsList ->
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


    fun updateTimer(): String {
        val totalSeconds = floor(currentPlayBackPosition / 1E3).toInt()
        val minutes = totalSeconds / 60
        val remainingSeconds = totalSeconds - (minutes * 60)
        timer = if (currentPlayBackPosition < 0) "--:--"
        else "%d:%02d".format(minutes, remainingSeconds)
        return timer
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

    fun playPlaylist(currentAudio: Song, songs: List<Song>) {
        serviceConnection.playAudio(songs)
        serviceConnection.transportControl
            .playFromMediaId(
                currentAudio.id.toString(),
                null
            )
    }

    fun shuffleAlbum(album: Album) {
        val number = Random.nextInt(from = 0, until = album.songCount)
        playPlaylist(currentAudio = album.songs[number], songs = album.songs)
    }

    fun shuffleArtist(artist: Artist) {
        val number = Random.nextInt(from = 0, until = artist.numberSongs)
        playPlaylist(currentAudio = artist.songs[number], songs = artist.songs)
    }

    fun shuffleSongs() {
        if (songList.size != 0) {
            val number = Random.nextInt(from = 0, until = songList.size)
            playPlaylist(currentAudio = songList[number], songList)
        }
    }

    fun shufflePlaylist(playlist: Playlist) {
        val number = Random.nextInt(from = 0, until = playlist.songCount)
        playPlaylist(currentAudio = playlist.songs[number], songs = playlist.songs)
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

    fun skipToPrevious() {
        serviceConnection.transportControl.skipToPrevious()
    }

    fun shuffle() {
        shuffleMode = !shuffleMode
        if (shuffleMode) {
            serviceConnection.transportControl.setShuffleMode(SHUFFLE_MODE_ALL)
        } else {
            serviceConnection.transportControl.setShuffleMode(SHUFFLE_MODE_NONE)
        }
    }

    fun repeat(number: Int = 1) {
        when (number) {
            1 -> serviceConnection.transportControl.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
            2 -> serviceConnection.transportControl.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
            3 -> serviceConnection.transportControl.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
        }
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

