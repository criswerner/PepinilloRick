package com.cristianwer.pepinillorick.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.domain.model.UiError

/**
 * Maps a [UiError] to a user-friendly localized string.
 */
@Composable
internal fun UiError.asString(): String {
    return when (this) {
        is UiError.Connection -> stringResource(id = R.string.error_connection)
        is UiError.Server -> stringResource(id = R.string.error_server, code ?: 0)
        is UiError.Unknown -> message ?: stringResource(id = R.string.error_unknown)
    }
}
