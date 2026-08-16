package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getCharactersFlow(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterByIdFlow(id: Int): Flow<CharacterEntity?>

    @Query("SELECT characters.* FROM characters INNER JOIN favorites ON characters.id = favorites.characterId")
    fun getFavoriteCharactersFlow(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
