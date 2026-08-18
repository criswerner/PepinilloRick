package com.cristianwer.pepinillorick.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.dao.CharacterWithFavoriteEntity
import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.dao.RemoteKeysDao
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import com.cristianwer.pepinillorick.data.local.entity.RemoteKeysEntity

@Database(
    entities = [CharacterEntity::class, RemoteKeysEntity::class, FavoriteEntity::class],
    views = [CharacterWithFavoriteEntity::class],
    version = 5,
    exportSchema = false
)
internal abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val favoriteDao: FavoriteDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "rick_and_morty_db"
    }
}
