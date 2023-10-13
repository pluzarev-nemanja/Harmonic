package com.example.mymusic.domain.use_cases

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InsertArtists @Inject constructor(
    private val repository: MusicRepository,
    @ApplicationContext val context: Context
) {

    private var mCursor: Cursor? = null
    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.ArtistColumns.ARTIST,
        MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS,
        MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS,
    )

    private val sortOrder = "${MediaStore.Audio.ArtistColumns.ARTIST} ASC"

    suspend operator fun invoke() {
        val artist = getCursorData()
        repository.insertArtist(artist)
    }

    private fun getCursorData(): MutableList<Artist> {
        val artistList = mutableListOf<Artist>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        mCursor?.use { cursor ->
            val artistName =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST)
            val numAlbum =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS)
            val numSongs =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS)


            cursor.apply {
                if (count == 0) {
                    Log.e("Cursor Artist", "getCursorData: Cursor is Empty")
                } else {
                    while (cursor.moveToNext()) {
                        val artName = getString(artistName)
                        val numAlbm = getInt(numAlbum)
                        val numberSongs = getInt(numSongs)
                        artistList += Artist(
                            artist = artName,
                            numberSongs = numberSongs,
                            numberAlbums = numAlbm
                        )
                    }
                }
            }
        }
        return artistList
    }

}