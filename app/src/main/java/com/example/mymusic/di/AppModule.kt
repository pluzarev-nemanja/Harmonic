package com.example.mymusic.di

import android.app.Application
import androidx.room.Room
import com.example.mymusic.data.data_source.MusicDatabase
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
}