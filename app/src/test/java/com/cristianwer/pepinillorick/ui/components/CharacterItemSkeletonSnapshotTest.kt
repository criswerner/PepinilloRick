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
 * Snapshot tests for [CharacterItemSkeleton] component using Roborazzi.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CharacterItemSkeletonSnapshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Captures a snapshot of [CharacterItemSkeleton] in dark theme.
     */
    @Test
    fun characterItemSkeleton_darkTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = true) {
                CharacterItemSkeleton()
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    /**
     * Captures a snapshot of [CharacterItemSkeleton] in light theme.
     */
    @Test
    fun characterItemSkeleton_lightTheme_snapshot() {
        composeTestRule.setContent {
            PepinilloRickTheme(darkTheme = false) {
                CharacterItemSkeleton()
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}
