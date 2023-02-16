package com.example.mymusic.presentation.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.use_cases.MusicUseCases
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaSource
@Inject constructor(private val musicUseCases: MusicUseCases) {
    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()

    var audioMediaMetaData: List<MediaMetadataCompat> = emptyList()
    var data : List<Song> = emptyList()

    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (
                value == AudioSourceState.STATE_CREATED
                || value == AudioSourceState.STATE_ERROR
            ) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener: OnReadyListener ->
                        listener.invoke(isReady)
                    }
                }
            } else {
                field = value
            }


        }

    suspend fun load() {
        state = AudioSourceState.STATE_INITIALIZING

        CoroutineScope(Dispatchers.IO).launch {
            musicUseCases.getAllSongs().collect {
                data = it
                audioMediaMetaData = data.map { song ->
                    MediaMetadataCompat.Builder()
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                            song.id.toString()
                        ).putString(
                            MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                            song.artist
                        ).putString(
                            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                            song.uri.toString()
                        ).putString(
                            MediaMetadataCompat.METADATA_KEY_TITLE,
                            song.title
                        ).putString(
                            MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                            song.displayName
                        ).putLong(
                            MediaMetadataCompat.METADATA_KEY_DURATION,
                            song.duration.toLong()
                        )
                        .build()
                }
                state = AudioSourceState.STATE_INITIALIZED

            }
        }
    }

    fun asMediaSource(dataSource: CacheDataSource.Factory):
            ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()

        audioMediaMetaData.forEach { mediaMetadataCompat ->
            val mediaItem = MediaItem.fromUri(
                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
            )

            val mediaSource = ProgressiveMediaSource
                .Factory(dataSource)
                .createMediaSource(mediaItem)

            concatenatingMediaSource.addMediaSource(mediaSource)


        }
        return concatenatingMediaSource

    }


    fun asMediaItem() = audioMediaMetaData.map { metaData ->
        val description = MediaDescriptionCompat.Builder()
            .setTitle(metaData.description.title)
            .setMediaId(metaData.description.mediaId)
            .setSubtitle(metaData.description.subtitle)
            .setMediaUri(metaData.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)

    }.toMutableList()


    fun refresh() {
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }


    fun whenReady(listener: OnReadyListener): Boolean {
        return if (
            state == AudioSourceState.STATE_CREATED
            || state == AudioSourceState.STATE_INITIALIZING
        ) {
            onReadyListeners += listener
            false
        } else {
            listener.invoke(isReady)
            true
        }

    }


    private val isReady: Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED


}

enum class AudioSourceState {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}

typealias OnReadyListener = (Boolean) -> Unit