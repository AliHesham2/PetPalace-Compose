package com.alagamb.petcompose.data.model.pet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.alagamb.petcompose.data.db.table.announcement.AnnouncementTable
import com.alagamb.petcompose.data.db.table.category.CategoryTable
import com.alagamb.petcompose.data.db.table.pet.PetTable
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.data.model.product.ProductItem

fun AnnouncementTable.toDomain(): AnnouncementItem = AnnouncementItem(
    id = id,
    tag = tag,
    title = title,
    description = description,
    ctaText = ctaText,
    backgroundBrush = Brush.linearGradient(
        colors = listOf(Color(startColor), Color(endColor))
    )
)

fun CategoryTable.toDomain(): PetCategoryItem {
    val icon = when (iconType.uppercase()) {
        "FLUTTER_DASH" -> Icons.Default.FlutterDash
        "WATER_DROP" -> Icons.Default.WaterDrop
        "CRUELTY_FREE" -> Icons.Default.CrueltyFree
        else -> Icons.Default.Pets
    }
    return PetCategoryItem(
        id = id,
        name = name,
        icon = icon,
        backgroundColor = Color(backgroundColor),
        iconTint = Color(iconTintColor)
    )
}

fun PetTable.toFeaturedDomain(): FeaturedPetItem = FeaturedPetItem(
    id = id,
    name = name,
    breed = breed,
    age = age,
    gender = gender,
    distance = distance,
    isFavorite = isFavorite,
    cardGradient = Brush.verticalGradient(
        colors = listOf(Color(startColor), Color(endColor))
    )
)

fun PetTable.toProductItemDomain(): ProductItem = ProductItem(
    id = id,
    name = name,
    category = categoryName,
    description = description,
    price = price,
    rating = rating,
    reviewCount = reviewCount,
    isFavorite = isFavorite,
    tag = tag,
    cardGradient = Brush.verticalGradient(
        colors = listOf(Color(startColor), Color(endColor))
    )
)
