@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.alagamb.petcompose.ui.commonui.customize

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified


// ── Solid background + clip ────────────────────────────────────────────────


fun Modifier.appBackground(
    color: Color,
    shape: Shape = AppShape.None,
): Modifier = this
    .clip(shape)
    .background(color = color, shape = shape)

// ── Gradient background + clip ─────────────────────────────────────────────

fun Modifier.appGradientBackground(
    colors: List<Color>,
    shape: Shape = AppShape.None,
    isVertical: Boolean = false,
): Modifier {
    val brush = if (isVertical) {
        Brush.verticalGradient(colors)
    } else {
        Brush.horizontalGradient(colors)
    }
    return this
        .clip(shape)
        .background(brush = brush, shape = shape)
}


fun Modifier.appRadialBackground(
    colors: List<Color>,
    shape: Shape = AppShape.None,
): Modifier = this
    .clip(shape)
    .background(brush = Brush.radialGradient(colors), shape = shape)

// ── Border / Stroke ────────────────────────────────────────────────────────

fun Modifier.appBorder(
    color: Color,
    shape: Shape = AppShape.None,
    width: Dp = 1.dp,
): Modifier = this.border(
    border = BorderStroke(width, color),
    shape  = shape,
)


fun Modifier.appGradientBorder(
    colors: List<Color>,
    shape: Shape = AppShape.None,
    width: Dp = 1.dp,
): Modifier = this.border(
    border = BorderStroke(width, Brush.linearGradient(colors)),
    shape  = shape,
)

// ── Background + Border combined (like a solid drawable with stroke) ────────

fun Modifier.appFill(
    fillColor: Color,
    borderColor: Color = Color.Transparent,
    shape: Shape = AppShape.None,
    borderWidth: Dp = 1.dp,
): Modifier = this
    .clip(shape)
    .background(color = fillColor, shape = shape)
    .border(BorderStroke(borderWidth, borderColor), shape)

// ── Shadow / Elevation ─────────────────────────────────────────────────────

fun Modifier.appShadow(
    elevation: Dp,
    shape: Shape = AppShape.None,
    ambientColor: Color = Color.Black.copy(alpha = 0.1f),
    spotColor: Color = Color.Black.copy(alpha = 0.25f),
): Modifier = this.shadow(
    elevation       = elevation,
    shape           = shape,
    ambientColor    = ambientColor,
    spotColor       = spotColor,
)

// ── Full card-style surface (background + shadow + clip) ───────────────────

fun Modifier.appSurface(
    fillColor: Color,
    shape: Shape = AppShape.None,
    shadowDp: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
): Modifier = this
    .shadow(elevation = shadowDp, shape = shape)
    .clip(shape)
    .background(color = fillColor, shape = shape)
    .then(
        if (borderWidth > 0.dp && borderColor != Color.Transparent)
            Modifier.border(BorderStroke(borderWidth, borderColor), shape)
        else Modifier
    )

// ─────────────────────────────────────────────────────────────────────────────
// AppModifier — Centralized layout & styling tokens across the application
// ─────────────────────────────────────────────────────────────────────────────
object AppModifier {

    // ── Shared / Common Modifiers across all screens ──────────────────────────
    object Shared {
        val root: Modifier = Modifier.fillMaxSize()
        val fillWidth: Modifier = Modifier.fillMaxWidth()

        fun screenRoot(backgroundColor: Color = Color.Unspecified): Modifier = Modifier
            .fillMaxSize()
            .then(if (backgroundColor != Color.Unspecified) Modifier.background(backgroundColor) else Modifier)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()

        fun contentScroll(maxWidth: Dp = Dp.Unspecified, scrollState: ScrollState): Modifier = Modifier
            .then(if (maxWidth.isSpecified) Modifier.widthIn(max = maxWidth) else Modifier)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .imeNestedScroll()
            .verticalScroll(scrollState)

        fun adaptiveWidth(isTablet: Boolean, isWideScreen: Boolean, defaultWidth: Dp = 500.dp): Dp = when {
            isTablet -> 440.dp
            isWideScreen -> 460.dp
            else -> defaultWidth
        }
    }

    // ── Registration (Land, Login, Register) ──────────────────────────────────
    object Registration {
        fun root(backgroundColor: Color): Modifier = Shared.screenRoot(backgroundColor)

        fun contentScroll(maxWidth: Dp, scrollState: ScrollState): Modifier =
            Shared.contentScroll(maxWidth, scrollState)

        fun calculateCardWidth(isTablet: Boolean, isWideScreen: Boolean): Dp =
            Shared.adaptiveWidth(isTablet, isWideScreen, defaultWidth = 500.dp)

        val topNavRow: Modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)

        val backButtonSurface: Modifier = Modifier.size(42.dp)

        val heroColumn: Modifier = Modifier.fillMaxWidth()

        val forgotPasswordRow: Modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp)

        val passwordRequirementNote: Modifier = Modifier.padding(top = 6.dp, start = 4.dp)

        val errorBanner: Modifier = Modifier.fillMaxWidth()

        val errorBannerRow: Modifier = Modifier.padding(14.dp)

        val submitButton: Modifier = Modifier.fillMaxWidth()

        val footerRow: Modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)

        // Land-specific
        val topBarRow: Modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp)

        val languageButtonPadding: Modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)

        val subtitleText: Modifier = Modifier.padding(horizontal = 16.dp)

        val featureBadgesRow: Modifier = Modifier.fillMaxWidth()

        val actionButtonsColumn: Modifier = Modifier.fillMaxWidth()

        val actionButton: Modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)

        val termsNote: Modifier = Modifier.padding(bottom = 16.dp, start = 12.dp, end = 12.dp)
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────
    object Dashboard {
        val root: Modifier = Modifier.fillMaxSize()

        val grid: Modifier = Modifier.fillMaxSize()

        val gridContentPadding: PaddingValues = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 12.dp,
            bottom = 28.dp
        )

        val itemSpanFull: Modifier = Modifier.fillMaxWidth()

        val fab: Modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)

        val searchTriggerSurface: Modifier = Modifier.height(52.dp)

        val searchTriggerContainer: Modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 640.dp)

        val searchTriggerRow: Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)

        val announcementCard: Modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)

        fun announcementCardAdaptive(isCompactHeight: Boolean): Modifier = Modifier
            .fillMaxWidth()
            .height(if (isCompactHeight) 128.dp else 154.dp)

        val announcementContent: Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)

        fun announcementContentAdaptive(isCompactHeight: Boolean): Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = if (isCompactHeight) 10.dp else 16.dp)

        fun announcementTagPadding(isCompactHeight: Boolean): Modifier = Modifier
            .padding(horizontal = 10.dp, vertical = if (isCompactHeight) 2.dp else 4.dp)

        fun announcementCtaPadding(isCompactHeight: Boolean): Modifier = Modifier
            .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 4.dp else 6.dp)

        val categoryItemColumn: Modifier = Modifier.width(62.dp)

        val categoryIconSurface: Modifier = Modifier.size(56.dp)

        val featuredCardImage: Modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)

        val featuredCardContent: Modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    }

    // ── Requests ──────────────────────────────────────────────────────────────
    object Requests {
        val root: Modifier = Modifier.fillMaxSize()

        fun contentContainer(maxWidth: Dp): Modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()

        val emptyContainer: Modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)

        val emptyCard: Modifier = Modifier.widthIn(max = 420.dp)

        val requestCard: Modifier = Modifier.fillMaxWidth()

        val cardHeader: Modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)

        val cardContent: Modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    }

    // ── Profile ───────────────────────────────────────────────────────────────
    object Profile {
        val root: Modifier = Modifier.fillMaxSize()

        val listContainer: Modifier = Modifier
            .widthIn(max = 520.dp)
            .fillMaxWidth()

        val listContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

        val heroCard: Modifier = Modifier.fillMaxWidth()

        val heroAvatar: Modifier = Modifier.size(80.dp)

        val sectionCard: Modifier = Modifier.fillMaxWidth()

        val sectionContent: Modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

        val statItem: Modifier = Modifier.width(80.dp)
    }

    // ── Add Pet ───────────────────────────────────────────────────────────────
    object AddPet {
        val root: Modifier = Modifier.fillMaxSize()

        val dualPaneContainer: Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .navigationBarsPadding()
            .imePadding()

        fun dualPaneFormColumn(scrollState: ScrollState): Modifier = Modifier
            .fillMaxHeight()
            .imeNestedScroll()
            .verticalScroll(scrollState)

        fun dualPanePreviewColumn(scrollState: ScrollState): Modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(scrollState)

        val singlePaneContainer: Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .navigationBarsPadding()
            .imePadding()

        fun singlePaneContent(scrollState: ScrollState): Modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .navigationBarsPadding()
            .imePadding()
            .imeNestedScroll()
            .verticalScroll(scrollState)

        val formColumn: Modifier = Modifier.fillMaxWidth()

        val previewCard: Modifier = Modifier.fillMaxWidth()

        val submitButton: Modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    }

    // ── Product List ──────────────────────────────────────────────────────────
    object ProductList {
        val root: Modifier = Modifier.fillMaxSize()

        val searchField: Modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

        val filterChipsRow: Modifier = Modifier.fillMaxWidth()

        val filterChipsPadding: PaddingValues = PaddingValues(horizontal = 16.dp)

        val resultsHeader: Modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)

        val grid: Modifier = Modifier.fillMaxSize()

        val gridContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

        val card: Modifier = Modifier.fillMaxWidth()

        val cardImage: Modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)

        val cardContent: Modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    }

    // ── Product Details ───────────────────────────────────────────────────────
    object ProductDetails {
        val root: Modifier = Modifier.fillMaxSize()

        val scrollContent: Modifier = Modifier.fillMaxSize()

        val bannerImage: Modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)

        val contentSection: Modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)

        val bottomBarSurface: Modifier = Modifier.fillMaxWidth()

        val bottomBarRow: Modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)

        val actionButton: Modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    }
}

typealias AppModifiers = AppModifier

