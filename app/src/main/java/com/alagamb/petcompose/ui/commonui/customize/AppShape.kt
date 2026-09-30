package com.alagamb.petcompose.ui.commonui.customize

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppShape — reusable Shape tokens (replaces XML <corners> tag)
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   Button(shape = AppShape.Pill) { ... }
//   Card(shape = AppShape.Large)  { ... }
//   Modifier.clip(AppShape.Medium)
//
// Analogy to XML:
//   <corners android:radius="8dp"/>   → AppShape.Small
//   <corners android:radius="12dp"/>  → AppShape.Medium
//   <corners android:radius="16dp"/>  → AppShape.Large
//   <corners android:radius="999dp"/> → AppShape.Pill  (CircleShape)
//   <corners android:radius="0dp"/>   → AppShape.None
// ─────────────────────────────────────────────────────────────────────────────

object AppShape {

    // ── Symmetric rounded corners ──────────────────────────────────────────

    /** No rounding — completely sharp corners (rectangle). */
    val None: Shape = RoundedCornerShape(0.dp)

    /** Extra small radius — 4 dp. Great for chips, badges. */
    val XSmall: Shape = RoundedCornerShape(4.dp)

    /** Small radius — 8 dp. Good for input fields, small cards. */
    val Small: Shape = RoundedCornerShape(8.dp)

    /** Medium radius — 12 dp. Default card / dialog corner. */
    val Medium: Shape = RoundedCornerShape(12.dp)

    /** Large radius — 16 dp. Panels, drawers, bottom sheets. */
    val Large: Shape = RoundedCornerShape(16.dp)

    /** Extra large radius — 24 dp. Hero cards, modals. */
    val XLarge: Shape = RoundedCornerShape(24.dp)

    /** Pill / Stadium — fully rounded ends (buttons, FABs). */
    val Pill: Shape = CircleShape
    // ── Top-only corners ───────────────────────────────────────────────────

    /** Only top corners rounded — 16 dp. Good for bottom sheets. */
    val TopMedium: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    /** Only top corners rounded — 28 dp. Bottom dialog / modal style. */
    val TopLarge: Shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    // ── Bottom-only corners ────────────────────────────────────────────────

    /** Only bottom corners rounded — 16 dp. Top-anchored dropdown panels. */
    val BottomMedium: Shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)

    // ── Asymmetric corners ─────────────────────────────────────────────────

    /** Start (left) corners rounded, end (right) sharp. Chat bubble — sent. */
    val BubbleEnd: Shape = RoundedCornerShape(
        topStart    = 16.dp,
        topEnd      = 16.dp,
        bottomStart = 16.dp,
        bottomEnd   = 4.dp,
    )

    /** End (right) corners rounded, start (left) sharp. Chat bubble — received. */
    val BubbleStart: Shape = RoundedCornerShape(
        topStart    = 4.dp,
        topEnd      = 16.dp,
        bottomStart = 16.dp,
        bottomEnd   = 16.dp,
    )

    // ── Cut corners ────────────────────────────────────────────────────────

    /** Cut corners — 8 dp diagonal slice on all 4 corners. */
    val CutSmall: Shape = CutCornerShape(8.dp)

    /** Cut corners — 16 dp diagonal slice on all 4 corners. */
    val CutLarge: Shape = CutCornerShape(16.dp)

    // ── Helper function for one-off custom corners ─────────────────────────

    /**
     * Create a [RoundedCornerShape] with individual corner control.
     *
     * Usage:
     * ```
     * Card(shape = AppShape.custom(topStart = 24.dp, bottomEnd = 24.dp)) { ... }
     * ```
     */
    fun custom(
        topStart: Dp = 0.dp,
        topEnd: Dp = 0.dp,
        bottomEnd: Dp = 0.dp,
        bottomStart: Dp = 0.dp,
    ): Shape = RoundedCornerShape(
        topStart    = topStart,
        topEnd      = topEnd,
        bottomEnd   = bottomEnd,
        bottomStart = bottomStart,
    )

    /**
     * Create a symmetric [RoundedCornerShape] with a given radius.
     *
     * Usage:
     * ```
     * Button(shape = AppShape.rounded(20.dp)) { ... }
     * ```
     */
    fun rounded(radius: Dp): Shape = RoundedCornerShape(radius)
}
