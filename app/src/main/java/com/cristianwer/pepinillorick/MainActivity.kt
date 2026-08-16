package com.cristianwer.pepinillorick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cristianwer.pepinillorick.ui.character_detail.CharacterDetailScreen
import com.cristianwer.pepinillorick.ui.character_list.CharacterListScreen
import com.cristianwer.pepinillorick.ui.favorite_list.FavoriteListScreen
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the application, serving as the single activity for all screens.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PepinilloRickTheme {
                RickAndMortyNavHost()
            }
        }
    }
}

/**
 * Navigation host for the application, defining all screens and their arguments.
 */
@Composable
private fun RickAndMortyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "character_list"
    ) {
        composable("character_list") {
            CharacterListScreen(
                viewModel = hiltViewModel(),
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                },
                onFavoritesClick = {
                    navController.navigate("favorite_list")
                }
            )
        }
        composable("favorite_list") {
            FavoriteListScreen(
                viewModel = hiltViewModel(),
                onCharacterClick = { characterId ->
                    navController.navigate("character_detail/$characterId")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "character_detail/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType }
            )
        ) {
            CharacterDetailScreen(
                viewModel = hiltViewModel(),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
