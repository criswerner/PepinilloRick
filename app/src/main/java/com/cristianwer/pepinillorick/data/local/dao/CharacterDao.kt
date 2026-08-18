package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * A Database View that combines characters with their favorite status.
 * It filters by the 'CHARACTER' type to ensure data integrity.
 */
@DatabaseView("""
    SELECT *, 
    EXISTS(SELECT 1 FROM favorites WHERE favorites.id = characters.id AND favorites.type = 'CHARACTER') AS isFavorite
    FROM characters
""")
internal data class CharacterWithFavoriteEntity(
    @Embedded val character: CharacterEntity,
    val isFavorite: Boolean
)

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM CharacterWithFavoriteEntity")
    fun getCharactersWithFavoriteFlow(): Flow<List<CharacterWithFavoriteEntity>>

    @Query("SELECT * FROM CharacterWithFavoriteEntity WHERE id = :id")
    suspend fun getCharacterWithFavoriteById(id: Int): CharacterWithFavoriteEntity?

    @Query("SELECT * FROM CharacterWithFavoriteEntity WHERE id = :id")
    fun getCharacterWithFavoriteByIdFlow(id: Int): Flow<CharacterWithFavoriteEntity?>

    @Query("SELECT * FROM CharacterWithFavoriteEntity WHERE isFavorite = 1")
    fun getFavoriteCharactersFlow(): Flow<List<CharacterWithFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
