package com.cristianwer.pepinillorick.ui.character_detail

import androidx.lifecycle.SavedStateHandle
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.pepinillorick.domain.usecase.GetCharacterByIdUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

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
        episodes = listOf("ep1")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load character when id is present in savedStateHandle`() = runTest {
        // Given
        val characterId = 1
        every { savedStateHandle.get<Int>("characterId") } returns characterId
        coEvery { getCharacterByIdUseCase(characterId) } returns sampleCharacter

        // When
        viewModel = CharacterDetailViewModel(getCharacterByIdUseCase, savedStateHandle)

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.character)
        assertEquals(sampleCharacter.name, state.character?.name)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `init should not load character when id is absent in savedStateHandle`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("characterId") } returns null

        // When
        viewModel = CharacterDetailViewModel(getCharacterByIdUseCase, savedStateHandle)

        // Then
        val state = viewModel.uiState.value
        assertNull(state.character)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `loadCharacter should update state with character details`() = runTest {
        // Given
        val characterId = 1
        every { savedStateHandle.get<Int>("characterId") } returns null
        coEvery { getCharacterByIdUseCase(characterId) } returns sampleCharacter
        viewModel = CharacterDetailViewModel(getCharacterByIdUseCase, savedStateHandle)

        // When
        viewModel.loadCharacter(characterId)

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.character)
        assertEquals(sampleCharacter.id, state.character?.id)
        assertEquals(false, state.isLoading)
    }
}
