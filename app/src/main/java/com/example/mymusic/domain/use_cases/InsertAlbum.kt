package com.example.mymusic.domain.use_cases

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InsertAlbum @Inject constructor(
    private val repository: MusicRepository,
    @ApplicationContext val context: Context
) {

    private var mCursor: Cursor? = null
    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AlbumColumns.ALBUM_ID,
        MediaStore.Audio.AlbumColumns.ALBUM,
        MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
        MediaStore.Audio.AlbumColumns.ARTIST,
    )
    private val sortOrder = "${MediaStore.Audio.AlbumColumns.ALBUM_ID} ASC"

    suspend operator fun invoke() {
        val albums = getCursorData()
        repository.insertAlbums(albums = albums)
    }

    private fun getCursorData(): MutableList<Album> {
        val albumList = mutableListOf<Album>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        mCursor?.use { cursor ->
            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM_ID)
            val album =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM)
            val numberOfSongs =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)
            val artist =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST)


            cursor.apply {
                if (count == 0) {
                    Log.e("Cursor Album", "getCursorData: Cursor is Empty")
                } else {
                    while (cursor.moveToNext()) {
                        val albumName = getString(album) ?: "Unknown"
                        val id = getString(idColumn) ?: ""
                        val numberSongs = getString(numberOfSongs) ?: "0"
                        val artistName = getString(artist) ?: "Unknown"
                        albumList+= Album(
                            albumId = id,
                            albumName = albumName,
                            songs = emptyList(),
                            songCount = numberSongs.toInt(),
                            artist = artistName,
                            albumImage = ""
                        )
                    }
                }
            }
        }
        return albumList
    }
}