package com.alagamb.petcompose.ui.theme

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.text.TextUtilsCompat
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import java.util.Locale

private class LocalizedActivityContext(
    base: Context,
    private val localizedResources: Resources
) : ContextWrapper(base) {
    override fun getResources(): Resources = localizedResources
}

@Composable
private fun ColorScheme.animated(
    animationSpec: AnimationSpec<Color> = tween(durationMillis = 400, easing = FastOutSlowInEasing)
): ColorScheme {
    val primary by animateColorAsState(this.primary, animationSpec, label = "primary")
    val onPrimary by animateColorAsState(this.onPrimary, animationSpec, label = "onPrimary")
    val primaryContainer by animateColorAsState(this.primaryContainer, animationSpec, label = "primaryContainer")
    val onPrimaryContainer by animateColorAsState(this.onPrimaryContainer, animationSpec, label = "onPrimaryContainer")
    val secondary by animateColorAsState(this.secondary, animationSpec, label = "secondary")
    val onSecondary by animateColorAsState(this.onSecondary, animationSpec, label = "onSecondary")
    val secondaryContainer by animateColorAsState(this.secondaryContainer, animationSpec, label = "secondaryContainer")
    val onSecondaryContainer by animateColorAsState(this.onSecondaryContainer, animationSpec, label = "onSecondaryContainer")
    val tertiary by animateColorAsState(this.tertiary, animationSpec, label = "tertiary")
    val onTertiary by animateColorAsState(this.onTertiary, animationSpec, label = "onTertiary")
    val background by animateColorAsState(this.background, animationSpec, label = "background")
    val onBackground by animateColorAsState(this.onBackground, animationSpec, label = "onBackground")
    val surface by animateColorAsState(this.surface, animationSpec, label = "surface")
    val onSurface by animateColorAsState(this.onSurface, animationSpec, label = "onSurface")
    val surfaceVariant by animateColorAsState(this.surfaceVariant, animationSpec, label = "surfaceVariant")
    val onSurfaceVariant by animateColorAsState(this.onSurfaceVariant, animationSpec, label = "onSurfaceVariant")
    val outline by animateColorAsState(this.outline, animationSpec, label = "outline")
    val outlineVariant by animateColorAsState(this.outlineVariant, animationSpec, label = "outlineVariant")
    val error by animateColorAsState(this.error, animationSpec, label = "error")
    val onError by animateColorAsState(this.onError, animationSpec, label = "onError")

    return this.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Brand.PurpleLight,
    onPrimary = Color(0xFF380094),
    primaryContainer = Color(0xFF4F00C7),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = AppColors.Brand.Teal,
    onSecondary = Color(0xFF003730),
    secondaryContainer = Color(0xFF005047),
    onSecondaryContainer = Color(0xFF66FFF8),
    tertiary = Color(0xFFFFB59D),
    onTertiary = Color(0xFF5B1B00),
    background = AppColors.Dark.Background,
    onBackground = AppColors.Dark.OnSurface,
    surface = AppColors.Dark.Surface,
    onSurface = AppColors.Dark.OnSurface,
    surfaceVariant = AppColors.Dark.SurfaceHigh,
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF8E9099),
    outlineVariant = Color(0xFF44474E),
    error = AppColors.Semantic.ErrorLight,
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Brand.Purple,
    onPrimary = AppColors.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = AppColors.Brand.TealDark,
    onSecondary = AppColors.White,
    secondaryContainer = Color(0xFFCEFAF6),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = Color(0xFFFF6B35),
    onTertiary = AppColors.White,
    background = Color(0xFFFBFBFE),
    onBackground = Color(0xFF1B1B1F),
    surface = AppColors.White,
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFF1F0F5),
    onSurfaceVariant = Color(0xFF46464F),
    outline = Color(0xFF767680),
    outlineVariant = Color(0xFFC7C5D0),
    error = AppColors.Semantic.Error,
    onError = AppColors.White
)

@Composable
fun PetComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep curated brand colors by default across light & dark
    dynamicColor: Boolean = false,
    language: String = "",
    content: @Composable () -> Unit
) {
    val rawColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val colorScheme = rawColorScheme.animated()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val activeLocaleTag = if (language.isNotBlank()) {
        language
    } else {
        AppCompatDelegate.getApplicationLocales()[0]?.language
            ?: configuration.locales[0]?.language
            ?: Locale.getDefault().language
    }

    val activeLocale = remember(activeLocaleTag) {
        Locale.forLanguageTag(if (activeLocaleTag.startsWith("ar", ignoreCase = true)) "ar" else "en")
    }
    val isRtl = activeLocale.language.startsWith("ar", ignoreCase = true)
        || TextUtilsCompat.getLayoutDirectionFromLocale(activeLocale) == View.LAYOUT_DIRECTION_RTL
    val layoutDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    val updatedConfig = remember(activeLocale, configuration) {
        Configuration(configuration).apply {
            setLocale(activeLocale)
            setLayoutDirection(activeLocale)
        }
    }

    val updatedContext = remember(activeLocale, context) {
        val configContext = context.createConfigurationContext(updatedConfig)
        LocalizedActivityContext(
            base = context,
            localizedResources = configContext.resources
        )
    }

    CompositionLocalProvider(
        LocalConfiguration provides updatedConfig,
        LocalContext provides updatedContext,
        LocalLayoutDirection provides layoutDirection
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}