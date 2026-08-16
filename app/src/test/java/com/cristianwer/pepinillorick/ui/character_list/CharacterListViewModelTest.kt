package com.cristianwer.pepinillorick.ui.character_list

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.SyncCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CharacterListViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterListViewModelTest {

    private lateinit var viewModel: CharacterListViewModel
    private val getCharactersUseCase: GetCharactersUseCase = mockk()
    private val syncCharactersUseCase: SyncCharactersUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    
    private val charactersFlow = MutableStateFlow<List<Character>>(emptyList())

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
        every { getCharactersUseCase() } returns charactersFlow
        coEvery { syncCharactersUseCase(any()) } returns Result.success(Unit)
        coEvery { toggleFavoriteUseCase(any(), any()) } returns Unit
        
        viewModel = CharacterListViewModel(getCharactersUseCase, syncCharactersUseCase, toggleFavoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleFavorite should call use case`() = runTest {
        // When
        viewModel.toggleFavorite(1, true)

        // Then
        coVerify { toggleFavoriteUseCase(1, true) }
    }

    @Test
    fun `initial uiState should not trigger synchronization`() = runTest {
        // Then
        io.mockk.coVerify(exactly = 0) { syncCharactersUseCase(any()) }
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `initial uiState should reflect characters from use case`() = runTest {
        // Given
        charactersFlow.value = listOf(sampleCharacter)

        // When
        val state = viewModel.uiState.value

        // Then
        assertEquals(1, state.characters.size)
        assertEquals(sampleCharacter.name, state.characters[0].name)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loadCharacters should update loading state during execution`() = runTest {
        // We start collecting the uiState to ensure stateIn is active
        val job = backgroundScope.launch { viewModel.uiState.collect {} }
        
        // Given
        coEvery { syncCharactersUseCase() } coAnswers {
            // Check state while "executing"
            assertEquals(true, viewModel.uiState.value.isLoading)
            Result.success(Unit)
        }

        // When
        viewModel.loadCharacters()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        job.cancel()
    }

    @Test
    fun `refresh should trigger sync with forceRefresh true`() = runTest {
        // Given
        coEvery { syncCharactersUseCase(forceRefresh = true) } returns Result.success(Unit)

        // When
        viewModel.refresh()

        // Then
        io.mockk.coVerify { syncCharactersUseCase(forceRefresh = true) }
    }
}
