package com.example.mymusic.presentation.songs

import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.util.OrderType
import com.example.mymusic.domain.util.SongOrder

data class SongState(
    val songs: List<Song> = emptyList(),
    val songOrder: SongOrder = SongOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
