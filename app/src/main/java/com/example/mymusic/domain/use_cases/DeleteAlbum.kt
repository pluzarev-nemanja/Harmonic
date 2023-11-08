package com.example.mymusic.domain.use_cases

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeleteAlbum @Inject constructor(
    private val repository: MusicRepository,
    @ApplicationContext val context: Context
) {

    suspend operator fun invoke(album: Album){
        repository.deleteAlbum(album)
    }

}