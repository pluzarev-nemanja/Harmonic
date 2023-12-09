package com.example.mymusic.di

import android.app.Application
import androidx.room.Room
import com.example.mymusic.data.data_source.MusicDatabase
import com.example.mymusic.data.repository.MusicRepositoryImpl
import com.example.mymusic.domain.repository.MusicRepository
import com.example.mymusic.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(app: Application): MusicDatabase {
        return Room.databaseBuilder(
            app,
            MusicDatabase::class.java,
            "music_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMusicRepository(db: MusicDatabase): MusicRepository {
        return MusicRepositoryImpl(db.musicDao)
    }

    @Provides
    @Singleton
    fun provideMusicUseCases(repository: MusicRepository, app: Application): MusicUseCases {
        return MusicUseCases(
            insertSong = InsertSong(repository, app),
            getAllSongsAsc = GetAllSongsAsc(repository),
            getAllSongsDesc = GetAllSongsDesc(repository),
            getAllSongsArtist = GetAllSongsArtist(repository),
            getAllSongsAlbum = GetAllSongsAlbum(repository),
            getAllSongsDate = GetAllSongsDate(repository),
            searchBySongName = SearchBySongName(repository),
            insertPlaylist = InsertPlaylist(repository),
            deletePlaylist = DeletePlaylist(repository),
            getAllPlaylists = GetAllPlaylists(repository),
            getAllPlaylistsDesc = GetAllPlaylistsDesc(repository),
            getAllPlaylistsDuration = GetAllPlaylistsDuration(repository),
            getAllPlaylistsSongCount = GetAllPlaylistsSongCount(repository),
            updatePlaylist = UpdatePlaylist(repository),
            insertAlbum = InsertAlbum(repository, app),
            getAllAlbumsAsc = GetAllAlbumsAsc(repository),
            getAllAlbumsArtist = GetAllAlbumsArtist(repository),
            getAllAlbumsSongCount = GetAllAlbumsSongCount(repository),
            getAlbumWithSongs = GetAlbumWithSongs(repository),
            insertHistory = InsertHistory(repository),
            getHistory = GetHistory(repository),
            insertFavorite = InsertFavorite(repository),
            getFavorite = GetFavorite(repository),
            updateSong = UpdateSong(repository),
            getArtists = GetArtists(repository),
            insertArtists = InsertArtists(repository, app),
            getArtistWithSongs = GetArtistWithSongs(repository),
            insertUser = InsertUser(repository),
            getUser = GetUser(repository),
            updateAlbum = UpdateAlbum(repository),
            updateArtist = UpdateArtist(repository),
            getPlayer = GetPlayer(repository),
            insertPlayer = InsertPlayer(repository)
        )
    }
}