package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing user favorites.
 */
@Dao
internal interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE characterId = :characterId)")
    fun isFavoriteFlow(characterId: Int): Flow<Boolean>

    @Query("SELECT characterId FROM favorites")
    fun getAllFavoriteIdsFlow(): Flow<List<Int>>
}
