package com.cristianwer.pepinillorick.domain.repository

import com.cristianwer.pepinillorick.domain.model.FavoriteType
import kotlinx.coroutines.flow.Flow

/**
 * Repository in charge of managing the user's favorites for any type of entity.
 */
internal interface FavoriteRepository {
    /**
     * Toggles the favorite status of an entity.
     *
     * @param id The unique identifier of the entity.
     * @param type The type of entity (Character, Episode, etc.).
     * @param isFavorite The new favorite status.
     */
    suspend fun toggleFavorite(id: Int, type: FavoriteType, isFavorite: Boolean)

    /**
     * Returns a stream of favorited IDs for a specific type.
     * 
     * @param type The type of entity.
     */
    fun getFavoriteIds(type: FavoriteType): Flow<List<Int>>

    /**
     * Checks if a specific entity is marked as favorite.
     * 
     * @param id The unique identifier of the entity.
     * @param type The type of entity.
     */
    fun isFavorite(id: Int, type: FavoriteType): Flow<Boolean>
}
