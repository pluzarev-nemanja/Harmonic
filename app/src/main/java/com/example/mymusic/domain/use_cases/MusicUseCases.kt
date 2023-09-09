package com.example.mymusic.domain.use_cases

data class MusicUseCases(
    val insertSong: InsertSong,
    val getAllSongsAsc: GetAllSongsAsc,
    val getAllSongsDesc: GetAllSongsDesc,
    val getAllSongsArtist: GetAllSongsArtist,
    val getAllSongsAlbum: GetAllSongsAlbum,
    val getAllSongsDate: GetAllSongsDate,
    val searchBySongName: SearchBySongName,
    val insertPlaylist: InsertPlaylist,
    val deletePlaylist: DeletePlaylist,
    val getAllPlaylists: GetAllPlaylists,
    val getAllPlaylistsDesc: GetAllPlaylistsDesc,
    val getAllPlaylistsDuration: GetAllPlaylistsDuration,
    val getAllPlaylistsSongCount: GetAllPlaylistsSongCount,
    val updatePlaylist: UpdatePlaylist,
    val insertAlbum: InsertAlbum,
    val deleteAlbum: DeleteAlbum,
    val getAllAlbumsAsc: GetAllAlbumsAsc
)
