package com.alagamb.petcompose.ui.commonui.pager

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip

// ─────────────────────────────────────────────────────────────────────────────
// AppPagerIndicator — reusable dot indicator for HorizontalPager / VerticalPager
// ─────────────────────────────────────────────────────────────────────────────
//
// Supports 5 animation styles via [AppIndicatorStyle]:
//   Worm  — active dot stretches into a pill (default)
//   Dot   — all same size, only color changes
//   Scale — active dot grows larger
//   Slide — a bar slides below the dots
//   Fade  — active dot is opaque, inactive are ghosted
//
// Usage:
//   AppPagerIndicator(pageCount = pages.size, pagerState = state)
//   AppPagerIndicator(pageCount = pages.size, pagerState = state,
//       style = AppPagerStyles.scale())
//   AppPagerIndicator(pageCount = pages.size, pagerState = state,
//       style = AppPagerStyles.white())
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A reusable dot/pill indicator that reads position live from [PagerState].
 *
 * Place this below your pager — it will automatically sync with swipe position.
 *
 * @param pageCount    Total number of pages.
 * @param pagerState   The [PagerState] from [rememberPagerState].
 * @param style        Visual style — controls colors, sizes, and animation type.
 *                     Defaults to [AppPagerStyles.worm].
 * @param modifier     Optional modifier (e.g. to align or add padding).
 */
@Composable
fun AppPagerIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle = AppPagerStyles.worm(),
    modifier: Modifier = Modifier,
) {
    when (style.indicatorStyle) {
        AppIndicatorStyle.Worm  -> WormIndicator(pageCount, pagerState, style, modifier)
        AppIndicatorStyle.Dot   -> DotIndicator(pageCount, pagerState, style, modifier)
        AppIndicatorStyle.Scale -> ScaleIndicator(pageCount, pagerState, style, modifier)
        AppIndicatorStyle.Slide -> SlideIndicator(pageCount, pagerState, style, modifier)
        AppIndicatorStyle.Fade  -> FadeIndicator(pageCount, pagerState, style, modifier)
    }
}

// ── Worm — active dot stretches into a pill ────────────────────────────────

@Composable
private fun WormIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle,
    modifier: Modifier,
) {
    val activeIndex = pagerState.currentPage.coerceAtMost((pageCount - 1).coerceAtLeast(0))
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(style.dotSpacing),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = activeIndex == index

            // Animate width: selected = wide pill, others = small circle
            val dotWidth by animateDpAsState(
                targetValue   = if (isSelected) style.activeWidth else style.dotSize,
                animationSpec = tween(durationMillis = 250),
                label         = "wormWidth$index",
            )

            Box(
                modifier = Modifier
                    .height(style.dotSize)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(if (isSelected) style.activeColor else style.inactiveColor),
            )
        }
    }
}

// ── Dot — all same size, only color changes ───────────────────────────────

@Composable
private fun DotIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle,
    modifier: Modifier,
) {
    val activeIndex = pagerState.currentPage.coerceAtMost((pageCount - 1).coerceAtLeast(0))
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(style.dotSpacing),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = activeIndex == index
            Box(
                modifier = Modifier
                    .size(style.dotSize)
                    .clip(CircleShape)
                    .background(if (isSelected) style.activeColor else style.inactiveColor),
            )
        }
    }
}

// ── Scale — active dot grows larger ──────────────────────────────────────

@Composable
private fun ScaleIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle,
    modifier: Modifier,
) {
    val activeIndex = pagerState.currentPage.coerceAtMost((pageCount - 1).coerceAtLeast(0))
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(style.dotSpacing),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = activeIndex == index

            val dotWidth by animateDpAsState(
                targetValue   = if (isSelected) (style.dotSize * 1.4f) else style.dotSize,
                animationSpec = tween(durationMillis = 250),
                label         = "scaleDot$index",
            )

            Box(
                modifier = Modifier
                    .size(dotWidth)
                    .clip(CircleShape)
                    .background(if (isSelected) style.activeColor else style.inactiveColor),
            )
        }
    }
}

// ── Slide — a bar slides below the dots ───────────────────────────────────

@Composable
private fun SlideIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle,
    modifier: Modifier,
) {
    val activeIndex = pagerState.currentPage.coerceAtMost((pageCount - 1).coerceAtLeast(0))
    // Step size = dotSize + dotSpacing
    val stepDp  = style.dotSize + style.dotSpacing
    val offsetX by animateDpAsState(
        targetValue   = stepDp * activeIndex,
        animationSpec = tween(durationMillis = 250),
        label         = "slideOffset",
    )

    Box(modifier = modifier) {
        // Inactive dots row (background)
        Row(
            horizontalArrangement = Arrangement.spacedBy(style.dotSpacing),
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { _ ->
                Box(
                    modifier = Modifier
                        .size(style.dotSize)
                        .clip(CircleShape)
                        .background(style.inactiveColor),
                )
            }
        }
        // Active sliding bar (foreground, positioned via offset)
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .size(style.dotSize)
                .clip(RoundedCornerShape(50))
                .background(style.activeColor),
        )
    }
}

// ── Fade — active is fully opaque, others ghost out ───────────────────────

@Composable
private fun FadeIndicator(
    pageCount: Int,
    pagerState: PagerState,
    style: AppPagerStyle,
    modifier: Modifier,
) {
    val activeIndex = pagerState.currentPage.coerceAtMost((pageCount - 1).coerceAtLeast(0))
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(style.dotSpacing),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isSelected = activeIndex == index

            val alpha by animateFloatAsState(
                targetValue   = if (isSelected) 1f else 0.3f,
                animationSpec = tween(durationMillis = 250),
                label         = "fadeDot$index",
            )

            Box(
                modifier = Modifier
                    .size(style.dotSize)
                    .alpha(alpha)
                    .clip(CircleShape)
                    .background(style.activeColor),
            )
        }
    }
}
