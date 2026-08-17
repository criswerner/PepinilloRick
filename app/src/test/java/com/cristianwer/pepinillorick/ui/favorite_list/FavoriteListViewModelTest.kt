package com.cristianwer.pepinillorick.ui.favorite_list

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.pepinillorick.domain.usecase.GetFavoriteCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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
 * Unit tests for [FavoriteListViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoriteListViewModelTest {

    private lateinit var viewModel: FavoriteListViewModel
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    
    private val favoritesFlow = MutableStateFlow<List<Character>>(emptyList())

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
        isFavorite = true
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getFavoriteCharactersUseCase() } returns favoritesFlow
        coEvery { toggleFavoriteUseCase(any(), any()) } returns Unit
        
        viewModel = FavoriteListViewModel(getFavoriteCharactersUseCase, toggleFavoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should be Empty when no favorites`() = runTest {
        // When & Then: Use first to wait for the first emission containing our expected state
        val state = viewModel.uiState.first { it !is FavoriteListUiState.Loading }
        assertTrue(state is FavoriteListUiState.Empty)
    }

    @Test
    fun `uiState should be Success when use case emits favorites`() = runTest {
        // Given
        favoritesFlow.value = listOf(sampleCharacter)

        // When & Then: Wait for success state
        val state = viewModel.uiState.first { it is FavoriteListUiState.Success }
        assertTrue(state is FavoriteListUiState.Success)
        val successState = state as FavoriteListUiState.Success
        assertEquals(1, successState.favorites.items.size)
        assertEquals(sampleCharacter.name, successState.favorites.items[0].name)
    }

    @Test
    fun `toggleFavorite should delegate to use case`() = runTest {
        // When
        viewModel.toggleFavorite(1, false)

        // Then
        coVerify { toggleFavoriteUseCase(1, false) }
    }
}
