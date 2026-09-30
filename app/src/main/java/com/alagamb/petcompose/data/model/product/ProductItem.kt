package com.alagamb.petcompose.data.model.product

import androidx.compose.ui.graphics.Brush

/**
 * Model representing an item in the Product List.
 */
data class ProductItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: String,
    val rating: Float,
    val reviewCount: Int,
    val isFavorite: Boolean = false,
    val tag: String? = null,
    val cardGradient: Brush,
)
