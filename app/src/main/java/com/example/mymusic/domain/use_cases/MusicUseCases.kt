package com.example.mymusic.domain.use_cases

data class MusicUseCases(
val insertSong: InsertSong,
val getAllSongsAsc: GetAllSongsAsc,
val getAllSongsDesc: GetAllSongsDesc,
val getAllSongsArtist: GetAllSongsArtist,
val getAllSongsAlbum: GetAllSongsAlbum,
val getAllSongsDate: GetAllSongsDate,
val searchBySongName: SearchBySongName
)
