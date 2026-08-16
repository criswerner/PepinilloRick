package com.cristianwer.pepinillorick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cristianwer.pepinillorick.ui.character_detail.CharacterDetailScreen
import com.cristianwer.pepinillorick.ui.character_list.CharacterListScreen
import com.cristianwer.pepinillorick.ui.favorite_list.FavoriteListScreen
import com.cristianwer.pepinillorick.ui.navigation.BottomNavItem
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the application, serving as the single activity for all screens.
 */
@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PepinilloRickTheme {
                RickAndMortyApp()
            }
        }
    }
}

/**
 * Main Composable that sets up the Scaffold with Bottom Navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RickAndMortyApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = remember {
        listOf(
            BottomNavItem.Characters,
            BottomNavItem.Favorites
        )
    }

    // Stabilize initial state: Default to showing bars and the first item's title
    // while the navController is initializing (currentDestination == null).
    val currentBottomNavItem = items.find { it.route == currentDestination?.route }
    val showBottomBar = currentDestination == null || currentBottomNavItem != null
    val currentTitleRes = currentBottomNavItem?.titleRes ?: BottomNavItem.Characters.titleRes

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = currentTitleRes))
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(stringResource(item.titleRes)) },
                            selected = selected,
                            onClick = {
                                if (currentDestination?.route != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Characters.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Characters.route) {
                CharacterListScreen(
                    viewModel = hiltViewModel(),
                    onCharacterClick = { characterId ->
                        navController.navigate("character_detail/$characterId")
                    }
                )
            }
            composable(BottomNavItem.Favorites.route) {
                FavoriteListScreen(
                    viewModel = hiltViewModel(),
                    onCharacterClick = { characterId ->
                        navController.navigate("character_detail/$characterId")
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
}
