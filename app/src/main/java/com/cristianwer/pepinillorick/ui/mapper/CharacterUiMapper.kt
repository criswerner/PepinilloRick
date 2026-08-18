package com.cristianwer.pepinillorick.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus

/**
 * Returns the appropriate icon for a character's gender.
 */
internal fun CharacterGender.getIcon(): ImageVector {
    return when (this) {
        CharacterGender.MALE -> Icons.Default.Male
        CharacterGender.FEMALE -> Icons.Default.Female
        CharacterGender.GENDERLESS,
        CharacterGender.UNKNOWN -> Icons.Default.Transgender
    }
}

/**
 * Returns the appropriate color for a character's status based on the current color scheme.
 */
@Composable
internal fun CharacterStatus.getColor(colorScheme: ColorScheme): Color {
    return when (this) {
        CharacterStatus.ALIVE -> colorScheme.primary
        CharacterStatus.DEAD -> colorScheme.error
        CharacterStatus.UNKNOWN -> colorScheme.onSurfaceVariant
    }
}
