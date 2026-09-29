package com.cristianwer.pepinillorick.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode

/**
 * Snapshot tests for [FavoriteButton] component using Roborazzi and Robolectric Native Graphics.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FavoriteButtonSnapshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Captures a snapshot of [FavoriteButton] when the character is marked as favorite.
     */
    @Test
    fun favoriteButton_markedAsFavorite_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme {
                FavoriteButton(
                    isFavorite = true,
                    onFavoriteClick = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [FavoriteButton] when the character is not marked as favorite.
     */
    @Test
    fun favoriteButton_notFavorite_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme {
                FavoriteButton(
                    isFavorite = false,
                    onFavoriteClick = {},
                )
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}
