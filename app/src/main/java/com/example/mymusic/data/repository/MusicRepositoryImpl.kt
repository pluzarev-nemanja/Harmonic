package com.example.mymusic.data.repository

import com.example.mymusic.data.data_source.MusicDao
import com.example.mymusic.data.relations.AlbumWithSongs
import com.example.mymusic.data.relations.ArtistWithSongs
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Favorite
import com.example.mymusic.domain.model.History
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Song
import com.example.mymusic.domain.model.User
import com.example.mymusic.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val dao: MusicDao
) : MusicRepository {

    override suspend fun insertSongs(songList: MutableList<Song>) {
        dao.insertSongs(songList)
    }

    override fun getAllSongsAsc(): Flow<List<Song>> {
        return dao.getAllSongsAsc()
    }

    override fun getAllSongsDesc(): Flow<List<Song>> {
        return dao.getAllSongsDesc()
    }

    override fun getAllSongsArtist(): Flow<List<Song>> {
        return dao.getAllSongsArtist()
    }

    override fun getAllSongsAlbum(): Flow<List<Song>> {
        return dao.getAllSongsAlbum()
    }

    override fun getAllSongsDate(): Flow<List<Song>> {
        return dao.getAllSongsDate()
    }

    override fun searchBySongName(searchQuery: String): Flow<List<Song>> {
        return dao.searchBySongName(searchQuery)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        dao.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        dao.deletePlaylist(playlist)
    }


    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return dao.getAllPlaylists()
    }

    override fun getAllPlaylistsDesc(): Flow<List<Playlist>> {
        return dao.getAllPlaylistsDesc()
    }

    override fun getAllPlaylistsSongCount(): Flow<List<Playlist>> {
        return dao.getAllPlaylistsSongCount()
    }

    override fun getAllPlaylistsDuration(): Flow<List<Playlist>> {
        return dao.getAllPlaylistsDuration()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        dao.updatePlaylist(playlist)
    }


    override suspend fun insertAlbums(albums: MutableList<Album>) {
        dao.insertAlbums(albums)
    }

    override fun getAllAlbums(): Flow<List<Album>> {
        return dao.getAllAlbums()
    }

    override fun getAllAlbumsArtist(): Flow<List<Album>> {
        return dao.getAllAlbumsArtist()
    }

    override fun getAllAlbumsSongCount(): Flow<List<Album>> {
        return dao.getAllAlbumsSongCount()
    }

    override fun getAlbumWithSongs(albumName: String): Flow<List<AlbumWithSongs>> {
        return dao.getAlbumWithSongs(albumName)
    }

    override suspend fun insertHistory(history: History) {
        dao.insertHistory(history)
    }

    override fun getHistory(id: Int): Flow<List<History>> {
        return dao.getHistory(id)
    }

    override fun getFavorite(id: Int): Flow<List<Favorite>> {
        return dao.getFavorite(id)
    }

    override suspend fun updateSong(song: Song) {
        dao.updateSong(song)
    }

    override suspend fun insertArtist(artist: MutableList<Artist>) {
        dao.insertArtist(artist)
    }

    override fun getArtists(): Flow<List<Artist>> {
        return dao.getArtists()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        dao.insertFavorite(favorite)
    }

    override fun getArtistWithSongs(artistName: String): Flow<List<ArtistWithSongs>> {
        return dao.getArtistWithSongs(artistName)
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun updateAlbum(album: Album) {
        dao.updateAlbum(album)
    }

    override suspend fun updateArtist(artist: Artist) {
        dao.updateArtist(artist)
    }

    override fun getUser(id: Int): Flow<List<User>> {
        return dao.getUser(id)
    }
}