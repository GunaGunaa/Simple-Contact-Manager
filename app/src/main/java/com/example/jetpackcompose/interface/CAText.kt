package com.example.jetpackcompose.`interface`

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface CAText {
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : CAText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(id = resId, formatArgs = args)
        }
    }
}