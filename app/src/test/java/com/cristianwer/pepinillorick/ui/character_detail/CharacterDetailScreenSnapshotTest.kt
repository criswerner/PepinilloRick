package com.cristianwer.pepinillorick.ui.character_detail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.ui.model.CharacterDetailUiModel
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode

/**
 * Snapshot tests for the full [CharacterDetailContent] screen component using Roborazzi.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CharacterDetailScreenSnapshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCharacterDetail = CharacterDetailUiModel(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = CharacterGender.MALE,
        originName = "Earth (C-137)",
        locationName = "Citadel of Ricks",
        imageUrl = "",
        episodeCount = 51,
        isFavorite = true,
    )

    /**
     * Captures a snapshot of [CharacterDetailContent] in [CharacterDetailUiState.Success] state in Dark Theme.
     */
    @Test
    fun characterDetail_successState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Success(sampleCharacterDetail),
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] in [CharacterDetailUiState.Success] state when character is not marked as favorite.
     */
    @Test
    fun characterDetail_notFavorite_successState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Success(
                        sampleCharacterDetail.copy(isFavorite = false)
                    ),
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] when status is DEAD and gender is FEMALE.
     */
    @Test
    fun characterDetail_deadStatusFemale_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Success(
                        sampleCharacterDetail.copy(
                            name = "Krombopulos Michael",
                            status = CharacterStatus.DEAD,
                            gender = CharacterGender.FEMALE,
                        )
                    ),
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] when status is UNKNOWN and gender is GENDERLESS.
     */
    @Test
    fun characterDetail_unknownStatusGenderless_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Success(
                        sampleCharacterDetail.copy(
                            name = "Alien Parasite",
                            status = CharacterStatus.UNKNOWN,
                            gender = CharacterGender.GENDERLESS,
                        )
                    ),
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] in [CharacterDetailUiState.Success] state in Light Theme.
     */
    @Test
    fun characterDetail_successState_lightTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = false) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Success(sampleCharacterDetail),
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] in [CharacterDetailUiState.Error] state in Dark Theme.
     */
    @Test
    fun characterDetail_errorState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Error,
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterDetailContent] in [CharacterDetailUiState.Loading] state in Dark Theme.
     */
    @Test
    fun characterDetail_loadingState_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterDetailContent(
                    uiState = CharacterDetailUiState.Loading,
                    onBackClick = {},
                    onFavoriteToggle = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}
