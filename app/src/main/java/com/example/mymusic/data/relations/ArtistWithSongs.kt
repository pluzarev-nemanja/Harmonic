package com.example.mymusic.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Song

data class ArtistWithSongs(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "artist",
        entityColumn = "artist"
    )
    val songs : List<Song>
)
