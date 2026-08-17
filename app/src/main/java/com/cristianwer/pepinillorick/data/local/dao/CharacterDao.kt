package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * POJO to represent a character combined with its favorite status from the database.
 */
internal data class CharacterWithFavoriteEntity(
    @Embedded val character: CharacterEntity,
    val isFavorite: Boolean
)

@Dao
internal interface CharacterDao {

    @Query("""
        SELECT *, 
        EXISTS(SELECT 1 FROM favorites WHERE favorites.characterId = characters.id) AS isFavorite 
        FROM characters
    """)
    fun getCharactersWithFavoriteFlow(): Flow<List<CharacterWithFavoriteEntity>>

    @Query("""
        SELECT *, 
        EXISTS(SELECT 1 FROM favorites WHERE favorites.characterId = characters.id) AS isFavorite 
        FROM characters WHERE id = :id
    """)
    suspend fun getCharacterWithFavoriteById(id: Int): CharacterWithFavoriteEntity?

    @Query("""
        SELECT *, 
        EXISTS(SELECT 1 FROM favorites WHERE favorites.characterId = characters.id) AS isFavorite 
        FROM characters WHERE id = :id
    """)
    fun getCharacterWithFavoriteByIdFlow(id: Int): Flow<CharacterWithFavoriteEntity?>

    @Query("SELECT characters.* FROM characters INNER JOIN favorites ON characters.id = favorites.characterId")
    fun getFavoriteCharactersFlow(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
