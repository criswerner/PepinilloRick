package com.cristianwer.pepinillorick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.cristianwer.pepinillorick.ui.character_detail.CharacterDetailScreen
import com.cristianwer.pepinillorick.ui.character_list.CharacterListScreen
import com.cristianwer.pepinillorick.ui.favorite_list.FavoriteListScreen
import com.cristianwer.pepinillorick.ui.navigation.BottomNavItem
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import com.cristianwer.pepinillorick.ui.theme.TranslucentBlack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PepinilloRickTheme {
                RickAndMortyApp()
            }
        }
    }
}

@Composable
private fun RickAndMortyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_tabs",
        modifier = Modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) },
        popEnterTransition = { fadeIn(animationSpec = tween(500)) },
        popExitTransition = { fadeOut(animationSpec = tween(500)) }
    ) {
        composable("main_tabs") {
            MainNavScreen(
                onCharacterClick = { id -> navController.navigate("character_detail/$id") }
            )
        }
        composable(
            route = "character_detail/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) {
            CharacterDetailScreen(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNavScreen(onCharacterClick: (Int) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = remember { listOf(BottomNavItem.Characters, BottomNavItem.Favorites) }
    val currentNavItem = items.find { it.route == currentDestination?.route } ?: BottomNavItem.Characters

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = currentNavItem.titleRes)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TranslucentBlack,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
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
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Characters.route,
                enterTransition = { fadeIn() }, 
                exitTransition = { fadeOut() }
            ) {
                composable(BottomNavItem.Characters.route) {
                    CharacterListScreen(
                        viewModel = hiltViewModel(),
                        onCharacterClick = onCharacterClick
                    )
                }
                composable(BottomNavItem.Favorites.route) {
                    FavoriteListScreen(
                        viewModel = hiltViewModel(),
                        onCharacterClick = onCharacterClick
                    )
                }
            }
        }
    }
}
