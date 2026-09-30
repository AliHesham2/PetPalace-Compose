package com.alagamb.petcompose.ui.commonui.pager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppPagerStyle — style tokens for AppHorizontalPager / AppVerticalPager
// ─────────────────────────────────────────────────────────────────────────────
//
// TWO independent style axes:
//
// 1. AppIndicatorStyle — controls how the DOT INDICATOR animates
//    Worm  → active dot stretches into a pill (default, modern look)
//    Dot   → all same size, active dot just changes color
//    Scale → active dot scales up, others stay small
//    Slide → a fixed bar slides underneath the dots
//    Fade  → dots fade in/out by opacity
//
// 2. AppPageTransition — controls how PAGES animate during a swipe
//    None     → standard slide (default Compose pager behavior)
//    Parallax → inner content moves slower → depth effect
//    Fade     → page fades out as you swipe away
//    Scale    → page shrinks as you swipe away
//    Depth    → page rotates + scales (book-flip feel)
//
// Usage:
//   AppHorizontalPager(state = state, style = AppPagerStyles.worm())   { ... }
//   AppHorizontalPager(state = state, style = AppPagerStyles.parallax()) { ... }
//   AppHorizontalPager(state = state, style = AppPagerStyles.depth())   { ... }
// ─────────────────────────────────────────────────────────────────────────────

// ── Indicator animation type ───────────────────────────────────────────────

/** Controls the visual animation style of the page dot indicator. */
enum class AppIndicatorStyle {
    /** Active dot stretches into a pill — modern, smooth look. */
    Worm,
    /** All dots same size; only color changes on selection. */
    Dot,
    /** Active dot scales to a larger circle, others shrink. */
    Scale,
    /** A fixed-height bar slides horizontally below the dots. */
    Slide,
    /** Active dot is fully opaque; inactive dots are semi-transparent. */
    Fade,
}

// ── Page transition animation type ────────────────────────────────────────

/** Controls how individual pages animate during a swipe gesture. */
enum class AppPageTransition {
    /** Standard pager slide — no extra transform applied. */
    None,
    /**
     * Parallax — page content slides at 30% speed relative to the container.
     * Creates a sense of depth (background vs foreground layer).
     */
    Parallax,
    /** Fade — page fades to transparent as you swipe it away. */
    Fade,
    /**
     * Scale — page shrinks from 100% → 85% as you swipe away.
     * Works great for onboarding cards and story-style pagers.
     */
    Scale,
    /**
     * Depth — page rotates on the Y-axis and scales down simultaneously.
     * Gives a book-flip / cube-rotation feel.
     */
    Depth,
}

// ── AppPagerStyle data class ───────────────────────────────────────────────

/**
 * Bundles all visual tokens for [AppHorizontalPager] and [AppVerticalPager].
 *
 * @param indicatorStyle    Animation style of the dot indicator.
 * @param pageTransition    Page-level animation during swipe.
 * @param activeColor       Color of the active/selected indicator element.
 * @param inactiveColor     Color of inactive/unselected indicator elements.
 * @param dotSize           Diameter of each dot in dp.
 * @param activeWidth       Width of the active element — only used by [AppIndicatorStyle.Worm]
 *                          and [AppIndicatorStyle.Slide]. Ignored by others.
 * @param dotSpacing        Gap between dots in dp.
 * @param showIndicator     Whether to render the indicator at all.
 */
@Stable
data class AppPagerStyle(
    val indicatorStyle: AppIndicatorStyle = AppIndicatorStyle.Worm,
    val pageTransition: AppPageTransition = AppPageTransition.None,
    val activeColor: Color,
    val inactiveColor: Color,
    val dotSize: Dp = 8.dp,
    val activeWidth: Dp = 24.dp,
    val dotSpacing: Dp = 6.dp,
    val showIndicator: Boolean = true,
)

// ── Pre-built pager styles ─────────────────────────────────────────────────

object AppPagerStyles {

    // ── Indicator-focused styles (None page transition) ──────────────────

    /**
     * Worm indicator — active dot stretches into a pill.
     * Standard slide page transition.
     */
    @Composable fun worm() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Dot indicator — all same size, active changes color only.
     * Minimalist look. Standard slide page transition.
     */
    @Composable fun dot() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Dot,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Scale indicator — active dot grows larger.
     * Standard slide page transition.
     */
    @Composable fun scale() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Scale,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
        dotSize        = 7.dp,
        activeWidth    = 13.dp,
    )

    /**
     * Slide indicator — a bar slides under the dots.
     * Standard slide page transition.
     */
    @Composable fun slide() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Slide,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Fade indicator — active dot is fully opaque, others are ghosted.
     * Standard slide page transition.
     */
    @Composable fun fade() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Fade,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
    )

    // ── Transition-focused styles (Worm indicator, fancy page animation) ─

    /**
     * Parallax — content moves slower than container during swipe.
     * Creates a cinematic depth effect. Best for image cards.
     */
    @Composable fun parallax() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.Parallax,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Fade transition — page fades in/out during swipe.
     * Great for full-screen image galleries.
     */
    @Composable fun fadeTransition() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Dot,
        pageTransition = AppPageTransition.Fade,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Scale transition — page shrinks as you swipe away.
     * Best for onboarding cards and story pagers.
     */
    @Composable fun scaleTransition() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.Scale,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /**
     * Depth transition — page rotates + scales (book-flip feel).
     * The most dramatic transition. Use for showcases.
     */
    @Composable fun depth() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Dot,
        pageTransition = AppPageTransition.Depth,
        activeColor    = MaterialTheme.colorScheme.primary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        dotSize        = 6.dp,
    )

    // ── Colored / branded styles ─────────────────────────────────────────

    /** Secondary — uses secondary color for the indicator dots. */
    @Composable fun secondary() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.None,
        activeColor    = MaterialTheme.colorScheme.secondary,
        inactiveColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
    )

    /** White — for pagers on dark/image backgrounds. */
    @Composable fun white() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.None,
        activeColor    = Color.White,
        inactiveColor  = Color.White.copy(alpha = 0.4f),
    )

    /** White + scale transition — ideal for hero onboarding on gradient bg. */
    @Composable fun onboarding() = AppPagerStyle(
        indicatorStyle = AppIndicatorStyle.Worm,
        pageTransition = AppPageTransition.Scale,
        activeColor    = Color.White,
        inactiveColor  = Color.White.copy(alpha = 0.4f),
    )
}
