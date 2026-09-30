package com.alagamb.petcompose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color
import com.alagamb.petcompose.ui.commonui.customize.AppColors

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
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}