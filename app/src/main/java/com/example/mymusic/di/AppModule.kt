package com.example.mymusic.di

import android.app.Application
import androidx.room.Room
import com.example.mymusic.data.data_source.MusicDatabase
import com.example.mymusic.data.repository.MusicRepositoryImpl
import com.example.mymusic.domain.repository.MusicRepository
import com.example.mymusic.domain.use_cases.GetAllSongs
import com.example.mymusic.domain.use_cases.InsertSong
import com.example.mymusic.domain.use_cases.MusicUseCases
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
    fun provideMusicRepository(db: MusicDatabase): MusicRepository{
        return MusicRepositoryImpl(db.musicDao)
    }

    @Provides
    @Singleton
    fun provideMusicUseCases(repository: MusicRepository,app: Application): MusicUseCases {
        return MusicUseCases(
            insertSong = InsertSong(repository,app),
            getAllSongs = GetAllSongs(repository)
        )
    }
}