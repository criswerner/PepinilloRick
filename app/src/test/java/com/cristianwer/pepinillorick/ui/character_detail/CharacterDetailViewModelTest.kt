package com.cristianwer.pepinillorick.ui.character_detail

import androidx.lifecycle.SavedStateHandle
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.pepinillorick.domain.usecase.ObserveCharacterUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CharacterDetailViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterDetailViewModelTest {

    private lateinit var viewModel: CharacterDetailViewModel
    private val observeCharacterUseCase: ObserveCharacterUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    
    private val characterFlow = MutableStateFlow<Character?>(null)

    private val sampleCharacter = Character(
        id = 1,
        name = "Rick",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = CharacterGender.MALE,
        origin = Location("Earth", "url"),
        location = Location("Earth", "url"),
        imageUrl = "image_url",
        episodes = listOf("ep1"),
        isFavorite = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { observeCharacterUseCase(any()) } returns characterFlow
        coEvery { toggleFavoriteUseCase(any(), any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should observe character when id is present in savedStateHandle`() = runTest {
        // Given
        val characterId = 1
        every { savedStateHandle.get<Int>("characterId") } returns characterId
        characterFlow.value = sampleCharacter

        // When
        viewModel = CharacterDetailViewModel(observeCharacterUseCase, toggleFavoriteUseCase, savedStateHandle)

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.character)
        assertEquals(sampleCharacter.name, state.character?.name)
    }

    @Test
    fun `toggleFavorite should call use case with current character info`() = runTest {
        // Given
        val characterId = 1
        every { savedStateHandle.get<Int>("characterId") } returns characterId
        characterFlow.value = sampleCharacter
        viewModel = CharacterDetailViewModel(observeCharacterUseCase, toggleFavoriteUseCase, savedStateHandle)

        // When
        viewModel.toggleFavorite()

        // Then
        coVerify { toggleFavoriteUseCase(characterId, true) }
    }
}
