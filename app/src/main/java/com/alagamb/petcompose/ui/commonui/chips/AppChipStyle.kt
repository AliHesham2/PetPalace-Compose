package com.alagamb.petcompose.ui.commonui.chips

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.ui.commonui.customize.AppShape

// ─────────────────────────────────────────────────────────────────────────────
// AppChipStyle — reusable style for FilterChip / InputChip (selectable chips)
// ─────────────────────────────────────────────────────────────────────────────
//
// ⚠️ Chip types use TWO different color types in M3:
//   • FilterChip / InputChip  → SelectableChipColors  (AppFilterChipStyle)
//   • AssistChip / SuggestionChip → ChipColors         (AppAssistChipStyle)
//
// Usage:
//   AppFilterChip(label = "All", selected = true,
//       colors = AppFilterChipStyles.filter().colors)
//   AppAssistChip(label = "Open Maps",
//       colors = AppAssistChipStyles.default().colors)
// ─────────────────────────────────────────────────────────────────────────────

// ── FilterChip / InputChip style (SelectableChipColors) ───────────────────

/**
 * Style token for [AppFilterChip] and [AppInputChip].
 * Uses [SelectableChipColors] which supports selected / unselected states.
 */
@Stable
data class AppFilterChipStyle(
    val colors: SelectableChipColors,
    val shape: Shape = AppShape.Pill,
    val border: BorderStroke? = null,
) {
    companion object {
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.surface,
            labelColor: Color = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledLabelColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            shape: Shape = AppShape.Pill,
            borderColor: Color = MaterialTheme.colorScheme.outline,
            borderWidth: Dp = 1.dp,
        ) = AppFilterChipStyle(
            colors = FilterChipDefaults.filterChipColors(
                containerColor          = containerColor,
                labelColor              = labelColor,
                selectedContainerColor  = selectedContainerColor,
                selectedLabelColor      = selectedLabelColor,
                disabledContainerColor  = disabledContainerColor,
                disabledLabelColor      = disabledLabelColor,
            ),
            shape  = shape,
            border = BorderStroke(borderWidth, borderColor),
        )
    }
}

object AppFilterChipStyles {

    /** Standard M3 filter chip — outlined, toggleable. */
    @Composable fun filter() = AppFilterChipStyle(
        colors = FilterChipDefaults.filterChipColors(),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Tag chip — always shows as "selected". Good for category tags. */
    @Composable fun tag() = AppFilterChipStyle(
        colors = FilterChipDefaults.filterChipColors(
            containerColor         = MaterialTheme.colorScheme.secondaryContainer,
            labelColor             = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor     = MaterialTheme.colorScheme.onPrimary,
        ),
        shape  = AppShape.Small,
    )

    /** Primary selected — uses primaryContainer as selected background. */
    @Composable fun primary() = AppFilterChipStyle(
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor     = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Error / danger chip — selected state shows error color. */
    @Composable fun danger() = AppFilterChipStyle(
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
            selectedLabelColor     = MaterialTheme.colorScheme.onErrorContainer,
        ),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )
}

// ── InputChip style (SelectableChipColors) ────────────────────────────────

/**
 * Style token for [AppInputChip].
 * Same [SelectableChipColors] as FilterChip but uses InputChipDefaults.
 */
@Stable
data class AppInputChipStyle(
    val colors: SelectableChipColors,
    val shape: Shape = AppShape.Pill,
    val border: BorderStroke? = null,
) {
    companion object {
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.surface,
            labelColor: Color = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
            shape: Shape = AppShape.Pill,
            borderColor: Color = MaterialTheme.colorScheme.outline,
            borderWidth: Dp = 1.dp,
        ) = AppInputChipStyle(
            colors = InputChipDefaults.inputChipColors(
                containerColor         = containerColor,
                labelColor             = labelColor,
                selectedContainerColor = selectedContainerColor,
                selectedLabelColor     = selectedLabelColor,
            ),
            shape  = shape,
            border = BorderStroke(borderWidth, borderColor),
        )
    }
}

object AppInputChipStyles {

    /** Default M3 input chip. */
    @Composable fun default() = AppInputChipStyle(
        colors = InputChipDefaults.inputChipColors(),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Selected / confirmed input chip — secondaryContainer bg. */
    @Composable fun selected() = AppInputChipStyle(
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor     = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        shape  = AppShape.Pill,
    )
}

// ── AssistChip style (ChipColors) ─────────────────────────────────────────

/**
 * Style token for [AppAssistChip].
 * Uses [ChipColors] (non-selectable) from [AssistChipDefaults].
 */
@Stable
data class AppAssistChipStyle(
    val colors: ChipColors,
    val shape: Shape = AppShape.Pill,
    val border: BorderStroke? = null,
) {
    companion object {
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.surface,
            labelColor: Color = MaterialTheme.colorScheme.onSurface,
            leadingIconColor: Color = MaterialTheme.colorScheme.primary,
            trailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape: Shape = AppShape.Pill,
            borderColor: Color = MaterialTheme.colorScheme.outline,
            borderWidth: Dp = 1.dp,
        ) = AppAssistChipStyle(
            colors = AssistChipDefaults.assistChipColors(
                containerColor    = containerColor,
                labelColor        = labelColor,
                leadingIconContentColor  = leadingIconColor,
                trailingIconContentColor = trailingIconColor,
            ),
            shape  = shape,
            border = BorderStroke(borderWidth, borderColor),
        )
    }
}

object AppAssistChipStyles {

    /** Standard M3 assist chip — outlined. */
    @Composable fun default() = AppAssistChipStyle(
        colors = AssistChipDefaults.assistChipColors(),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Elevated assist chip — surface bg, no outline. */
    @Composable fun elevated() = AppAssistChipStyle(
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape  = AppShape.Pill,
    )

    /** Tinted assist chip — uses secondaryContainer. */
    @Composable fun tinted() = AppAssistChipStyle(
        colors = AssistChipDefaults.assistChipColors(
            containerColor  = MaterialTheme.colorScheme.secondaryContainer,
            labelColor      = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        shape  = AppShape.Pill,
    )
}

// ── SuggestionChip style (ChipColors) ─────────────────────────────────────

/**
 * Style token for [AppSuggestionChip].
 * Uses [ChipColors] from [SuggestionChipDefaults].
 */
@Stable
data class AppSuggestionChipStyle(
    val colors: ChipColors,
    val shape: Shape = AppShape.Pill,
    val border: BorderStroke? = null,
) {
    companion object {
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.surface,
            labelColor: Color = MaterialTheme.colorScheme.onSurface,
            iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape: Shape = AppShape.Pill,
            borderColor: Color = MaterialTheme.colorScheme.outline,
            borderWidth: Dp = 1.dp,
        ) = AppSuggestionChipStyle(
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor         = containerColor,
                labelColor             = labelColor,
                iconContentColor       = iconColor,
            ),
            shape  = shape,
            border = BorderStroke(borderWidth, borderColor),
        )
    }
}

object AppSuggestionChipStyles {

    /** Standard M3 suggestion chip — outlined. */
    @Composable fun default() = AppSuggestionChipStyle(
        colors = SuggestionChipDefaults.suggestionChipColors(),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Filled suggestion chip — primaryContainer bg. Good for AI suggestions. */
    @Composable fun filled() = AppSuggestionChipStyle(
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor   = MaterialTheme.colorScheme.primaryContainer,
            labelColor       = MaterialTheme.colorScheme.onPrimaryContainer,
            iconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape  = AppShape.Pill,
    )
}
