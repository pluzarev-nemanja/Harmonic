package com.example.mymusic.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Song

data class AlbumWithSongs (
    @Embedded val album: Album,
    @Relation(
        parentColumn = "albumName",
        entityColumn = "album"
    )
    val songs : List<Song>
)