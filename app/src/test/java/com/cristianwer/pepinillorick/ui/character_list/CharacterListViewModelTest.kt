package com.cristianwer.pepinillorick.ui.character_list

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CharacterListViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterListViewModelTest {

    private lateinit var viewModel: CharacterListViewModel
    private val getCharactersUseCase: GetCharactersUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val charactersFlow = MutableStateFlow<Resource<List<Character>>>(Resource.Loading())

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
        every { getCharactersUseCase(any()) } returns charactersFlow
        coEvery { toggleFavoriteUseCase(any(), any()) } returns Unit
        
        viewModel = CharacterListViewModel(getCharactersUseCase, toggleFavoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should be InitialLoading`() = runTest {
        // Then
        assertTrue(viewModel.uiState.value is CharacterListUiState.InitialLoading)
    }

    @Test
    fun `uiState should be Success when database has characters`() = runTest {
        // Given
        charactersFlow.value = Resource.Success(listOf(sampleCharacter))

        // When & Then: Wait for the state to transition
        val state = viewModel.uiState.first { it is CharacterListUiState.Success }

        assertTrue(state is CharacterListUiState.Success)
        val successState = state as CharacterListUiState.Success
        assertEquals(1, successState.characters.items.size)
        assertEquals(sampleCharacter.name, successState.characters.items[0].name)
    }

    @Test
    fun `loadCharacters should trigger sync and keep Success state during pagination`() = runTest {
        // Given: Already have data in DB
        charactersFlow.value = Resource.Success(listOf(sampleCharacter))
        
        // Ensure initial success state is reached
        viewModel.uiState.first { it is CharacterListUiState.Success }
        
        // We start collecting to keep the flow active for triggers
        val job = backgroundScope.launch { viewModel.uiState.collect {} }

        // When
        viewModel.loadCharacters()

        // Then
        coVerify(atLeast = 1) { getCharactersUseCase(any()) }
        
        job.cancel()
    }

    @Test
    fun `loadCharacters should result in InitialError when DB is empty and sync fails`() = runTest {
        // Given: Empty DB
        charactersFlow.value = Resource.Loading()
        val errorMessage = "Network Error"
        
        // When
        viewModel.loadCharacters()
        charactersFlow.value = Resource.Error(errorMessage)

        // Then: Wait for error state
        val state = viewModel.uiState.first { it is CharacterListUiState.InitialError }
        assertTrue(state is CharacterListUiState.InitialError)
        assertEquals(errorMessage, (state as CharacterListUiState.InitialError).message)
    }

    @Test
    fun `toggleFavorite should delegate to use case`() = runTest {
        // When
        viewModel.toggleFavorite(1, true)

        // Then
        coVerify { toggleFavoriteUseCase(1, true) }
    }
}
