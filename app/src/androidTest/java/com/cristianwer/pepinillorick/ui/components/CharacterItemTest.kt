package com.cristianwer.pepinillorick.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [CharacterItem] component.
 */
class CharacterItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCharacter = CharacterUiModel(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        locationName = "Citadel of Ricks",
        isFavorite = true
    )

    @Test
    fun characterItem_displaysCorrectInformation() {
        // When
        composeTestRule.setContent {
            PepinilloRickTheme(dynamicColor = false) {
                CharacterItem(
                    character = sampleCharacter,
                    onCharacterClick = {},
                    onFavoriteToggle = { _, _ -> }
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText(sampleCharacter.name.uppercase()).assertIsDisplayed()
        
        val expectedStatusText = "${sampleCharacter.species} - ${sampleCharacter.status.value}"
        composeTestRule.onNodeWithText(expectedStatusText, substring = true).assertIsDisplayed()
        
        composeTestRule.onNodeWithText(sampleCharacter.locationName).assertIsDisplayed()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val contentDescription = context.getString(R.string.character_list_favorite)
        composeTestRule.onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    @Test
    fun characterItem_triggersOnClick() {
        var clickedId = -1
        
        // When
        composeTestRule.setContent {
            PepinilloRickTheme(dynamicColor = false) {
                CharacterItem(
                    character = sampleCharacter,
                    onCharacterClick = { clickedId = it },
                    onFavoriteToggle = { _, _ -> }
                )
            }
        }
        composeTestRule.onNodeWithText(sampleCharacter.name.uppercase()).performClick()

        // Then
        assert(clickedId == sampleCharacter.id)
    }

    @Test
    fun characterItem_triggersOnFavoriteToggle() {
        var toggledId = -1
        var toggledStatus = false
        
        // When
        composeTestRule.setContent {
            PepinilloRickTheme(dynamicColor = false) {
                CharacterItem(
                    character = sampleCharacter,
                    onCharacterClick = {},
                    onFavoriteToggle = { id, status -> 
                        toggledId = id
                        toggledStatus = status
                    }
                )
            }
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val contentDescription = context.getString(R.string.character_list_not_favorite)
        
        composeTestRule.onNodeWithContentDescription(contentDescription).performClick()

        // Then
        assert(toggledId == sampleCharacter.id)
        assert(toggledStatus == !sampleCharacter.isFavorite)
    }
}
