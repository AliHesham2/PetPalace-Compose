package com.alagamb.petcompose.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Wraps [onClick] with a time debounce to prevent rapid multi-clicks.
 *
 * @param debounceTimeMs Minimum interval in milliseconds between valid clicks (default 600ms).
 * @param onClick The action to execute.
 */
@Composable
fun rememberDebounceClick(
    debounceTimeMs: Long = 600L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return remember(onClick, debounceTimeMs) {
        {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime >= debounceTimeMs) {
                lastClickTime = now
                onClick()
            }
        }
    }
}

/**
 * A debounced [clickable] modifier that drops rapid repetitive taps.
 */
fun Modifier.debounceClickable(
    debounceTimeMs: Long = 600L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val debouncedClick = rememberDebounceClick(debounceTimeMs = debounceTimeMs, onClick = onClick)
    this.clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = debouncedClick
    )
}
