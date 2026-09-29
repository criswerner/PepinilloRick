package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode

/**
 * Snapshot tests for the full [FavoriteListContent] screen component using Roborazzi.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FavoriteListScreenSnapshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleFavoriteCharacter = CharacterUiModel(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        imageUrl = "",
        locationName = "Citadel of Ricks",
        isFavorite = true,
    )

    /**
     * Captures a snapshot of [FavoriteListContent] in [FavoriteListUiState.Success] state in Dark Theme.
     */
    @Test
    fun favoriteList_successState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                FavoriteListContent(
                    uiStateProvider = {
                        FavoriteListUiState.Success(persistentListOf(sampleFavoriteCharacter))
                    },
                    onCharacterClick = {},
                    onFavoriteToggle = { _, _ -> },
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [FavoriteListContent] in [FavoriteListUiState.Success] state in Light Theme.
     */
    @Test
    fun favoriteList_successState_lightTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = false) {
                FavoriteListContent(
                    uiStateProvider = {
                        FavoriteListUiState.Success(persistentListOf(sampleFavoriteCharacter))
                    },
                    onCharacterClick = {},
                    onFavoriteToggle = { _, _ -> },
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [FavoriteListContent] in [FavoriteListUiState.Empty] state in Dark Theme.
     */
    @Test
    fun favoriteList_emptyState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                FavoriteListContent(
                    uiStateProvider = { FavoriteListUiState.Empty },
                    onCharacterClick = {},
                    onFavoriteToggle = { _, _ -> },
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [FavoriteListContent] in [FavoriteListUiState.Loading] state in Dark Theme.
     */
    @Test
    fun favoriteList_loadingState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                FavoriteListContent(
                    uiStateProvider = { FavoriteListUiState.Loading },
                    onCharacterClick = {},
                    onFavoriteToggle = { _, _ -> },
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}
