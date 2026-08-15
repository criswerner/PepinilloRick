package com.cristianwer.pepinillorick.ui.character_list

import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.SyncCharactersUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCharactersUseCase() } returns flowOf(emptyList())
        coEvery { syncCharactersUseCase(any()) } returns Result.success(Unit)
        
        viewModel = CharacterListViewModel(getCharactersUseCase, syncCharactersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should have default title`() = runTest {
        // When
        val state = viewModel.uiState.first()

        // Then
        assertEquals("Personajes", state.title)
    }

    @Test
    fun `loadCharacters should update loading state`() = runTest {
        // Given
        coEvery { syncCharactersUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.loadCharacters()

        // Then
        val state = viewModel.uiState.value
        assertEquals(2, state.currentPage)
        assertEquals(false, state.isLoading)
    }
}
