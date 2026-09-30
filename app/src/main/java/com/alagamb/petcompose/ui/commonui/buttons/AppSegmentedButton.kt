package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// ─────────────────────────────────────────────────────────────────────────────
// AppSingleSegmentedButton  (exclusive selection — like radio buttons)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Single-choice segmented button row.
 *
 * Usage:
 * ```
 * var selected by remember { mutableIntStateOf(0) }
 * AppSingleSegmentedButton(
 *     options       = listOf("Day", "Week", "Month"),
 *     selectedIndex = selected,
 *     onOptionSelected = { selected = it }
 * )
 * ```
 *
 * @param options          List of labels to display as segments.
 * @param selectedIndex    Index of the currently selected option.
 * @param onOptionSelected Called with the new index when a segment is tapped.
 * @param modifier         Optional layout modifier.
 * @param colors           Override active/inactive container and content colors.
 */
@Composable
fun AppSingleSegmentedButton(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick  = { onOptionSelected(index) },
                shape    = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                colors   = colors,
            ) {
                Text(label)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppMultiSegmentedButton  (multi-select checkboxes in pill form)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Multi-choice segmented button row — multiple options can be selected simultaneously.
 *
 * Usage:
 * ```
 * val selected = remember { mutableStateSetOf(0) }
 * AppMultiSegmentedButton(
 *     options       = listOf("Bold", "Italic", "Underline"),
 *     selectedSet   = selected,
 *     onOptionToggled = { index, isChecked ->
 *         if (isChecked) selected.add(index) else selected.remove(index)
 *     }
 * )
 * ```
 *
 * @param options         List of labels.
 * @param selectedSet     Set of currently selected indices.
 * @param onOptionToggled Called with (index, isNowChecked) on each tap.
 * @param modifier        Optional layout modifier.
 * @param colors          Override active/inactive colors.
 */
@Composable
fun AppMultiSegmentedButton(
    options: List<String>,
    selectedSet: Set<Int>,
    onOptionToggled: (index: Int, isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
) {
    MultiChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                checked         = index in selectedSet,
                onCheckedChange = { isChecked -> onOptionToggled(index, isChecked) },
                shape           = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                colors          = colors,
            ) {
                Text(label)
            }
        }
    }
}
