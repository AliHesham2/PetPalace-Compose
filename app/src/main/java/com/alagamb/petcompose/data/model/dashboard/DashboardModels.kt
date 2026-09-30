package com.alagamb.petcompose.data.model.dashboard

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Model representing an announcement card in the dashboard carousel.
 */
data class AnnouncementItem(
    val id: String,
    val tag: String,
    val title: String,
    val description: String,
    val ctaText: String,
    val backgroundBrush: Brush,
)

/**
 * Model representing a pet category item in the horizontal categories row.
 * Displays a pastel circular icon container with a label below.
 */
data class PetCategoryItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconTint: Color,
)

/**
 * Model representing a featured pet card in the 2-column grid.
 */
data class FeaturedPetItem(
    val id: String,
    val name: String,
    val breed: String,
    val age: String,
    val gender: String,
    val distance: String,
    val isFavorite: Boolean = false,
    val cardGradient: Brush,
)
