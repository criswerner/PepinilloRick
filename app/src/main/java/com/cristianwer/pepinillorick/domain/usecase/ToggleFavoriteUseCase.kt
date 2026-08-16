package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import javax.inject.Inject

/**
 * Use case to toggle the favorite status of a Rick & Morty character.
 *
 * @property repository The repository used to update favorite status.
 */
internal class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to toggle a character's favorite status.
     *
     * @param id The unique identifier of the character.
     * @param isFavorite The new favorite status to set.
     */
    suspend operator fun invoke(id: Int, isFavorite: Boolean) {
        repository.toggleFavorite(id, isFavorite)
    }
}
