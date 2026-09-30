package com.alagamb.petcompose.ui.screens.registration

import com.alagamb.petcompose.ui.commonui.brand.AppLogo

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.R
import com.alagamb.petcompose.ui.commonui.buttons.AppButton
import com.alagamb.petcompose.ui.commonui.buttons.AppButtonStyles
import com.alagamb.petcompose.ui.commonui.buttons.AppOutlinedButton
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.theme.PetComposeTheme

@Composable
fun LandRoute(
    onClick: (isLogin: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    LandScreen(
        onClick = onClick,
        modifier = modifier,
        windowAdaptiveInfo = windowAdaptiveInfo
    )
}

@Composable
fun LandScreen(
    onClick: (isLogin: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val scrollState = rememberScrollState()
    val activeLocale = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
    var currentLang by remember { mutableStateOf(activeLocale) }

    fun toggleLanguage() {
        val nextLang = if (currentLang == "ar") "en" else "ar"
        currentLang = nextLang
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(nextLang))
    }

    val isTablet = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isWideScreen = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    val maxCardWidth = when {
        isTablet -> 440.dp
        isWideScreen -> 460.dp
        else -> 500.dp
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxCardWidth)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top Language Switcher Bar ────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = AppShape.Pill,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier
                        .clip(AppShape.Pill)
                        .clickable { toggleLanguage() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = stringResource(R.string.profile_section_language),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (currentLang == "ar") "العربية 🇸🇦" else "English 🇺🇸",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Hero Section with Brand Illustration ─────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Petify App Logo
                AppLogo(
                    size = 100.dp,
                    elevation = 8.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // App Title
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline Headline
                Text(
                    text = stringResource(R.string.land_start),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle Description
                Text(
                    text = stringResource(R.string.land_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Highlight Feature Badges ─────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LandingFeatureBadge(
                        icon = Icons.Default.Verified,
                        text = stringResource(R.string.land_feature_verified)
                    )
                    LandingFeatureBadge(
                        icon = Icons.Default.HealthAndSafety,
                        text = stringResource(R.string.land_feature_health)
                    )
                    LandingFeatureBadge(
                        icon = Icons.Default.Favorite,
                        text = stringResource(R.string.land_feature_adoption)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Call to Action Buttons ───────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    text = stringResource(R.string.land_get_started),
                    onClick = { onClick(false) },
                    style = AppButtonStyles.primary(),
                    shape = AppShape.Pill,
                    leadingIcon = Icons.Default.Pets,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                AppOutlinedButton(
                    text = stringResource(R.string.land_login_button),
                    onClick = { onClick(true) },
                    shape = AppShape.Pill,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Legal Disclaimer
                Text(
                    text = stringResource(R.string.land_terms_note),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp, start = 12.dp, end = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun LandingFeatureBadge(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = AppShape.Pill,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ──  PREVIEW  ────────────────────────────────────────────────

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun LandScreenPreview() {
    PetComposeTheme {
        LandScreen(onClick = {})
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun LandScreenDarkPreview() {
    PetComposeTheme(darkTheme = true) {
        LandScreen(onClick = {})
    }
}

@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp,dpi=420",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LandScreenFoldablePreview() {
    PetComposeTheme {
        LandScreen(onClick = {})
    }
}

@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LandScreenTabletPreview() {
    PetComposeTheme {
        LandScreen(onClick = {})
    }
}