package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing user favorites generically.
 */
@Dao
internal interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id AND type = :type)")
    fun isFavoriteFlow(id: Int, type: String): Flow<Boolean>

    @Query("SELECT id FROM favorites WHERE type = :type")
    fun getFavoriteIdsByTypeFlow(type: String): Flow<List<Int>>
}
