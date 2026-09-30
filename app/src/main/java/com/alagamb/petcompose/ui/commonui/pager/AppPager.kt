package com.alagamb.petcompose.ui.commonui.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlin.math.absoluteValue

// ─────────────────────────────────────────────────────────────────────────────
// AppHorizontalPager / AppVerticalPager
// ─────────────────────────────────────────────────────────────────────────────
//
// Drop-in wrappers around Compose's HorizontalPager / VerticalPager.
// Adds:
//   1. Page TRANSITION animations via [AppPageTransition]
//   2. Optional built-in [AppPagerIndicator] below the pager
//
// The page content is wrapped in a [graphicsLayer] that reads
// [PagerState.currentPageOffsetFraction] to compute the transform.
// Because graphicsLayer is applied AFTER layout, it has zero recomposition cost.
//
// Usage — basic:
//   val state = rememberPagerState(pageCount = { items.size })
//   AppHorizontalPager(state = state) { page -> MyPageContent(items[page]) }
//
// Usage — with style:
//   AppHorizontalPager(
//       state          = state,
//       style          = AppPagerStyles.scaleTransition(),
//       showIndicator  = true,
//   ) { page -> MyPageContent(items[page]) }
//
// Usage — with custom indicator alignment:
//   AppHorizontalPager(
//       state               = state,
//       style               = AppPagerStyles.white(),
//       indicatorAlignment  = Alignment.End,
//   ) { page -> MyPageContent(items[page]) }
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A horizontal swipe pager with optional page transitions and a built-in indicator.
 *
 * @param state               [PagerState] from [rememberPagerState].
 * @param modifier            Outer modifier applied to the pager container.
 * @param style               Visual + animation style. Controls both the indicator
 *                            and the page transition. Defaults to [AppPagerStyles.worm].
 * @param showIndicator       When true, renders an [AppPagerIndicator] below the pager.
 *                            Defaults to true. Ignored if [style.showIndicator] is false.
 * @param indicatorAlignment  Horizontal alignment of the dot indicator row.
 * @param indicatorPadding    Vertical spacing between pager content and indicator.
 * @param beyondViewportPageCount Pages to keep composed past the visible viewport (0 = default).
 * @param content             Page composable — receives the page [Int] index.
 */
@Composable
fun AppHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    style: AppPagerStyle = AppPagerStyles.worm(),
    showIndicator: Boolean = true,
    indicatorAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    indicatorPadding: Dp = 12.dp,
    pageSize: PageSize = PageSize.Fill,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSpacing: Dp = 0.dp,
    beyondViewportPageCount: Int = 0,
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    Column(modifier = modifier) {
        HorizontalPager(
            state                   = state,
            modifier                = Modifier.fillMaxWidth(),
            pageSize                = pageSize,
            contentPadding          = contentPadding,
            pageSpacing             = pageSpacing,
            beyondViewportPageCount = beyondViewportPageCount,
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPageTransition(
                        pagerState = state,
                        page       = page,
                        transition = style.pageTransition,
                    ),
            ) {
                content(page)
            }
        }

        val visiblePages = (pageSize as? AdaptivePageSize)?.visiblePages ?: 1f
        val isScrollable = if (state.layoutInfo.visiblePagesInfo.isNotEmpty()) {
            state.canScrollForward || state.canScrollBackward
        } else {
            state.pageCount > 1
        }

        val effectivePageCount = if (visiblePages > 1f) {
            if (state.pageCount <= visiblePages) {
                0
            } else {
                (state.pageCount - visiblePages.toInt() + 1).coerceAtLeast(1)
            }
        } else {
            state.pageCount
        }

        if (showIndicator && style.showIndicator && effectivePageCount > 1 && isScrollable) {
            Box(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(top = indicatorPadding),
                contentAlignment  = when (indicatorAlignment) {
                    Alignment.Start  -> Alignment.CenterStart
                    Alignment.End    -> Alignment.CenterEnd
                    else             -> Alignment.Center
                },
            ) {
                AppPagerIndicator(
                    pageCount = effectivePageCount,
                    pagerState = state,
                    style = style,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Adaptive Page Size Helpers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * An adaptive [PageSize] implementation that tracks the number of [visiblePages]
 * configured for the current window breakpoint.
 */
class AdaptivePageSize(
    val visiblePages: Float,
) : PageSize {
    override fun Density.calculateMainAxisPageSize(availableSpace: Int, pageSpacing: Int): Int {
        return if (visiblePages <= 1f) {
            availableSpace
        } else {
            ((availableSpace - pageSpacing) / visiblePages).toInt().coerceAtLeast(0)
        }
    }
}

object AppPageSizes {
    /**
     * Creates an adaptive [AdaptivePageSize] that smoothly scales the number of visible pages
     * across different window sizes (Phones, Foldables, Tablets).
     *
     * @param windowAdaptiveInfo Information about current window size and posture.
     * @param compactVisiblePages Number of pages visible on compact screens (phones). Defaults to 1f.
     * @param mediumVisiblePages Number of pages visible on medium screens (foldables). Defaults to 1.5f (peek).
     * @param expandedVisiblePages Number of pages visible on expanded screens (tablets). Defaults to 2f.
     */
    fun adaptive(
        windowAdaptiveInfo: WindowAdaptiveInfo,
        compactVisiblePages: Float = 1f,
        mediumVisiblePages: Float = 1.5f,
        expandedVisiblePages: Float = 2f,
    ): AdaptivePageSize {
        val visiblePages = when {
            windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> expandedVisiblePages
            windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> mediumVisiblePages
            else -> compactVisiblePages
        }

        return AdaptivePageSize(visiblePages)
    }
}

/**
 * Remembers an adaptive [AdaptivePageSize] based on [WindowAdaptiveInfo].
 *
 * Examples:
 * - Phone: 1 card (full width)
 * - Foldable (unfolded): 1.5 cards (1 card + half card peek)
 * - Tablet: 2 cards (side-by-side)
 */
@Composable
fun rememberAdaptivePageSize(
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    compactVisiblePages: Float = 1f,
    mediumVisiblePages: Float = 1.5f,
    expandedVisiblePages: Float = 2f,
): AdaptivePageSize {
    return remember(windowAdaptiveInfo, compactVisiblePages, mediumVisiblePages, expandedVisiblePages) {
        AppPageSizes.adaptive(
            windowAdaptiveInfo = windowAdaptiveInfo,
            compactVisiblePages = compactVisiblePages,
            mediumVisiblePages = mediumVisiblePages,
            expandedVisiblePages = expandedVisiblePages,
        )
    }
}

/**
 * A vertical swipe pager with optional page transitions.
 *
 * Vertical pagers (like Instagram Reels / TikTok) rarely need a dot indicator,
 * so [showIndicator] defaults to false here. Set it to true if you need one.
 *
 * @param state               [PagerState] from [rememberPagerState].
 * @param modifier            Outer modifier applied to the pager container.
 * @param style               Visual + animation style.
 * @param showIndicator       Whether to render an [AppPagerIndicator]. Default false.
 * @param beyondViewportPageCount Pages to keep composed past the visible viewport.
 * @param content             Page composable — receives the page [Int] index.
 */
@Composable
fun AppVerticalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    style: AppPagerStyle = AppPagerStyles.worm(),
    showIndicator: Boolean = false,
    beyondViewportPageCount: Int = 0,
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    Column(modifier = modifier) {
        VerticalPager(
            state                   = state,
            modifier                = Modifier.weight(1f),
            beyondViewportPageCount = beyondViewportPageCount,
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPageTransition(
                        pagerState = state,
                        page       = page,
                        transition = style.pageTransition,
                    ),
            ) {
                content(page)
            }
        }

        if (showIndicator && style.showIndicator && state.pageCount > 1) {
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                AppPagerIndicator(
                    pageCount = state.pageCount,
                    pagerState = state,
                    style = style,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private: graphicsLayer transforms per AppPageTransition
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Applies the [AppPageTransition] to a [Modifier] via [graphicsLayer].
 * This runs entirely in the draw phase — zero recomposition triggered.
 */
private fun Modifier.applyPageTransition(
    pagerState: PagerState,
    page: Int,
    transition: AppPageTransition,
): Modifier {
    if (transition == AppPageTransition.None) return this

    return this.graphicsLayer {
        // How far this page is from center (0.0 = current, ±1.0 = one page away)
        val pageOffset = pagerState
            .currentPageOffsetFraction
            .plus(pagerState.currentPage - page)
            .absoluteValue

        when (transition) {

            AppPageTransition.None -> { /* nothing — handled above */ }

            // Parallax — content moves at 30% of page speed → depth effect
            AppPageTransition.Parallax -> {
                val rawOffset = (pagerState.currentPage - page +
                        pagerState.currentPageOffsetFraction)
                translationX = rawOffset * size.width * 0.3f
            }

            // Fade — page becomes transparent as it scrolls out of view
            AppPageTransition.Fade -> {
                alpha = lerpClamped(start = 0.4f, stop = 1f, fraction = 1f - pageOffset)
            }

            // Scale — page shrinks from 100% → 85% as it leaves the viewport
            AppPageTransition.Scale -> {
                val scale = lerpClamped(start = 0.85f, stop = 1f, fraction = 1f - pageOffset)
                scaleX = scale
                scaleY = scale
            }

            // Depth — rotation + scale: book-flip / cube feel
            AppPageTransition.Depth -> {
                val scale = lerpClamped(start = 0.75f, stop = 1f, fraction = 1f - pageOffset)
                scaleX        = scale
                scaleY        = scale
                alpha         = lerpClamped(start = 0.5f, stop = 1f, fraction = 1f - pageOffset)
                rotationY     = pageOffset * -30f   // rotate up to 30° on Y axis
                cameraDistance = 12f * density       // push camera back for perspective
            }
        }
    }
}

/**
 * Linear interpolation clamped to [0f, 1f].
 * [fraction] of 0 → [start], 1 → [stop].
 */
private fun lerpClamped(start: Float, stop: Float, fraction: Float): Float {
    val t = fraction.coerceIn(0f, 1f)
    return start + (stop - start) * t
}
