package com.alagamb.petcompose.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal providing a root-level [SnackbarHostState].
 * Allows any screen or route to display global snackbars without prop drilling.
 */
val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided. Make sure to wrap your hierarchy with CompositionLocalProvider(LocalSnackbarHostState provides ...)")
}
