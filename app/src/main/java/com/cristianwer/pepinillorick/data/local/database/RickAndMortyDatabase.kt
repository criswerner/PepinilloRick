package com.cristianwer.pepinillorick.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao

    companion object {
        const val DATABASE_NAME = "rick_and_morty_db"
    }
}
