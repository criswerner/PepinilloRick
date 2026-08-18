package com.cristianwer.pepinillorick.di

import android.content.Context
import androidx.room.Room
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.dao.RemoteKeysDao
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            RickAndMortyDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: RickAndMortyDatabase): CharacterDao {
        return database.characterDao
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: RickAndMortyDatabase): RemoteKeysDao {
        return database.remoteKeysDao
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: RickAndMortyDatabase): FavoriteDao {
        return database.favoriteDao
    }
}
