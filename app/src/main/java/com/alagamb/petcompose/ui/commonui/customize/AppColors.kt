package com.alagamb.petcompose.ui.commonui.customize

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


object AppColors {

    // ── Brand palette ──────────────────────────────────────────────────────

    object Brand {
        val Purple       = Color(0xFF6200EA)
        val PurpleLight  = Color(0xFF9D46FF)
        val PurpleDark   = Color(0xFF0A00B6)
        val Teal         = Color(0xFF03DAC5)
        val TealLight    = Color(0xFF66FFF8)
        val TealDark     = Color(0xFF00A896)
    }

    // ── Semantic states ────────────────────────────────────────────────────

    object Semantic {
        /** ✅ Success — confirmed, saved, completed */
        val Success        = Color(0xFF2E7D32)
        val SuccessLight   = Color(0xFF81C784)
        val SuccessSurface = Color(0xFFE8F5E9)

        /** ⚠️ Warning — caution, pending, risky */
        val Warning        = Color(0xFFF9A825)
        val WarningLight   = Color(0xFFFFD54F)
        val WarningSurface = Color(0xFFFFF8E1)

        /** ❌ Error / Danger — failure, destructive actions */
        val Error          = Color(0xFFB00020)
        val ErrorLight     = Color(0xFFEF9A9A)
        val ErrorSurface   = Color(0xFFFFEBEE)

        /** ℹ️ Info — neutral information, tips */
        val Info           = Color(0xFF0277BD)
        val InfoLight      = Color(0xFF81D4FA)
        val InfoSurface    = Color(0xFFE1F5FE)
    }

    // ── Neutral grays ──────────────────────────────────────────────────────

    object Gray {
        val Gray50  = Color(0xFFFAFAFA)
        val Gray100 = Color(0xFFF5F5F5)
        val Gray200 = Color(0xFFEEEEEE)
        val Gray300 = Color(0xFFE0E0E0)
        val Gray400 = Color(0xFFBDBDBD)
        val Gray500 = Color(0xFF9E9E9E)
        val Gray600 = Color(0xFF757575)
        val Gray700 = Color(0xFF616161)
        val Gray800 = Color(0xFF424242)
        val Gray900 = Color(0xFF212121)
    }

    // ── Dark surface palette (for dark-mode style cards/dialogs) ───────────

    object Dark {
        val Background  = Color(0xFF0F0F0F)
        val Surface     = Color(0xFF1A1A1A)
        val SurfaceHigh = Color(0xFF242424)
        val OnSurface   = Color(0xFFE0E0E0)
        val OnSurfaceMuted = Color(0xFF9E9E9E)
    }

    // ── Gradient presets ────────────────────────────────────────────────────
    //  Use with Modifier.appGradientBackground(AppColors.Gradient.PurpleTeal)

    object Gradient {
        val PurpleTeal   = listOf(Color(0xFF6200EA), Color(0xFF03DAC5))
        val SunsetWarm   = listOf(Color(0xFFFF6B35), Color(0xFFF7C59F))
        val OceanDeep    = listOf(Color(0xFF1A237E), Color(0xFF00BCD4))
        val NightSky     = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
        val GreenMint    = listOf(Color(0xFF2E7D32), Color(0xFF00BFA5))
        val RoseGold     = listOf(Color(0xFFB5338A), Color(0xFFFF8A65))
        val SilverGray   = listOf(Color(0xFFBDBDBD), Color(0xFFF5F5F5))
    }

    // ── Transparent helpers ────────────────────────────────────────────────

    val Transparent = Color.Transparent
    val Black       = Color.Black
    val White       = Color.White

    /** Black at 60% — standard scrim/overlay for dialogs, bottom sheets. */
    val Scrim       = Color(0x99000000)

    /** Black at 20% — subtle image overlay for legibility. */
    val ImageOverlay = Color(0x33000000)
}




