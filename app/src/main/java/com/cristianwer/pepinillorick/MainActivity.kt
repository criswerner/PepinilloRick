package com.cristianwer.pepinillorick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.cristianwer.pepinillorick.ui.character_list.CharacterListScreen
import com.cristianwer.pepinillorick.ui.theme.PepinilloRickTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PepinilloRickTheme {
                CharacterListScreen(viewModel = hiltViewModel())
            }
        }
    }
}
