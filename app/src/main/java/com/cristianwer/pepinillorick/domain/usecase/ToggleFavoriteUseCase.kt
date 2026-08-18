package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.FavoriteType
import com.cristianwer.pepinillorick.domain.repository.FavoriteRepository
import javax.inject.Inject

/**
 * Use case to toggle the favorite status of a character.
 */
internal class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * Executes the action of marking/unmarking a character as favorite.
     *
     * @param id The unique identifier of the character.
     * @param isFavorite The new favorite status.
     */
    suspend operator fun invoke(id: Int, isFavorite: Boolean) {
        favoriteRepository.toggleFavorite(id, FavoriteType.CHARACTER, isFavorite)
    }
}
