package com.alagamb.petcompose.ui.screens.products.productlist

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.alagamb.petcompose.data.model.product.ProductItem

object ProductListMockData {

    val allProducts = listOf(
        // Dogs
        ProductItem(
            id = "prod_dog_1",
            name = "Royal Canine Adult Kibble",
            category = "Dogs",
            description = "Complete nutrition formulated for medium & large adult dogs.",
            price = "$34.99",
            rating = 4.8f,
            reviewCount = 142,
            isFavorite = true,
            tag = "Best Seller",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
            )
        ),
        ProductItem(
            id = "prod_dog_2",
            name = "Orthopedic Memory Bed",
            category = "Dogs",
            description = "High-density joint relief foam with washable plush cover.",
            price = "$59.99",
            rating = 4.9f,
            reviewCount = 89,
            isFavorite = false,
            tag = "Popular",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
            )
        ),
        ProductItem(
            id = "prod_dog_3",
            name = "Tough Rope Chew Toy",
            category = "Dogs",
            description = "Natural cotton fibers for dental cleaning & aggressive chewers.",
            price = "$12.50",
            rating = 4.6f,
            reviewCount = 64,
            isFavorite = false,
            tag = "Discount",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFCCBC), Color(0xFFFFAB91))
            )
        ),
        ProductItem(
            id = "prod_dog_4",
            name = "Reflective No-Pull Harness",
            category = "Dogs",
            description = "Breathable padded vest with dual leash clips for safety.",
            price = "$26.00",
            rating = 4.7f,
            reviewCount = 110,
            isFavorite = false,
            tag = "New",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFE8D6), Color(0xFFFFD1A9))
            )
        ),

        // Cats
        ProductItem(
            id = "prod_cat_1",
            name = "Multi-Level Cat Tree Tower",
            category = "Cats",
            description = "Sisal-covered scratching posts with cozy plush perches.",
            price = "$79.99",
            rating = 4.9f,
            reviewCount = 203,
            isFavorite = true,
            tag = "Best Seller",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
            )
        ),
        ProductItem(
            id = "prod_cat_2",
            name = "Salmon & Tuna Pate (Pack of 12)",
            category = "Cats",
            description = "Grain-free protein rich wet food loaded with essential taurine.",
            price = "$22.99",
            rating = 4.7f,
            reviewCount = 78,
            isFavorite = false,
            tag = "Popular",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
            )
        ),
        ProductItem(
            id = "prod_cat_3",
            name = "Interactive Feather Wand",
            category = "Cats",
            description = "Flexible carbon fiber rod with natural feathers and bells.",
            price = "$8.99",
            rating = 4.5f,
            reviewCount = 52,
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFD8E4), Color(0xFFFFB1C8))
            )
        ),
        ProductItem(
            id = "prod_cat_4",
            name = "Self-Cleaning Litter Mat",
            category = "Cats",
            description = "Double-layer honeycomb design captures scattered litter easily.",
            price = "$18.50",
            rating = 4.6f,
            reviewCount = 41,
            isFavorite = false,
            tag = "New",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFF8BBD0), Color(0xFFF48FB1))
            )
        ),

        // Birds
        ProductItem(
            id = "prod_bird_1",
            name = "Gourmet Seed & Fruit Mix",
            category = "Birds",
            description = "Fortified vitamin blend for cockatiels, parakeets, & conures.",
            price = "$15.99",
            rating = 4.7f,
            reviewCount = 63,
            isFavorite = false,
            tag = "Best Seller",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFE2EED8), Color(0xFFC5E1A5))
            )
        ),
        ProductItem(
            id = "prod_bird_2",
            name = "Natural Wood Perch Playground",
            category = "Birds",
            description = "Chewable non-toxic wooden climbing ladder & hanging swing.",
            price = "$24.99",
            rating = 4.8f,
            reviewCount = 37,
            isFavorite = true,
            tag = "Popular",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFDCEDC8), Color(0xFFAED581))
            )
        ),
        ProductItem(
            id = "prod_bird_3",
            name = "Hanging Mineral Calcium Block",
            category = "Birds",
            description = "Essential beak conditioning stone with natural iodine & minerals.",
            price = "$6.49",
            rating = 4.6f,
            reviewCount = 29,
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8))
            )
        ),

        // Fish
        ProductItem(
            id = "prod_fish_1",
            name = "Color-Enhancing Tropical Flakes",
            category = "Fish",
            description = "Formulated with carotenoids to bring out brilliant vibrant fish colors.",
            price = "$11.99",
            rating = 4.6f,
            reviewCount = 94,
            isFavorite = false,
            tag = "Popular",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFDBE9F6), Color(0xFF90CAF9))
            )
        ),
        ProductItem(
            id = "prod_fish_2",
            name = "Ultra-Quiet Aquarium Water Filter",
            category = "Fish",
            description = "3-stage biological and mechanical filtration for tanks up to 20 gallons.",
            price = "$32.50",
            rating = 4.8f,
            reviewCount = 51,
            isFavorite = true,
            tag = "Best Seller",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA))
            )
        ),
        ProductItem(
            id = "prod_fish_3",
            name = "LED Aquarium Hood Lighting",
            category = "Fish",
            description = "Full spectrum plant growth light with daylight and moonlight modes.",
            price = "$28.00",
            rating = 4.7f,
            reviewCount = 38,
            isFavorite = false,
            tag = "New",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
            )
        ),

        // Others (Rabbits, Hamsters, etc.)
        ProductItem(
            id = "prod_other_1",
            name = "First-Cut Western Timothy Hay",
            category = "Others",
            description = "High-fiber sweet fragrant hay essential for rabbit & guinea pig digestion.",
            price = "$19.99",
            rating = 4.9f,
            reviewCount = 118,
            isFavorite = true,
            tag = "Best Seller",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFBE0E5), Color(0xFFF48FB1))
            )
        ),
        ProductItem(
            id = "prod_other_2",
            name = "Silent Spinner Running Wheel",
            category = "Others",
            description = "Quiet ball-bearing spinner wheel ideal for hamsters & hedgehogs.",
            price = "$16.50",
            rating = 4.7f,
            reviewCount = 65,
            isFavorite = false,
            tag = "Popular",
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF0F5), Color(0xFFF8BBD0))
            )
        ),
        ProductItem(
            id = "prod_other_3",
            name = "Natural Apple Wood Chew Sticks",
            category = "Others",
            description = "Organic pesticide-free branches to keep small animal teeth trimmed.",
            price = "$7.99",
            rating = 4.8f,
            reviewCount = 44,
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
            )
        )
    )

    /**
     * Featured items collection (curated popular and best-rated products)
     */
    val featuredProducts: List<ProductItem> = allProducts.filter {
        it.tag == "Best Seller" || it.tag == "Popular" || it.isFavorite
    }

    /**
     * Gets products filtered by category name, or returns all/featured if matching title
     */
    fun getProductsForTitle(title: String): List<ProductItem> {
        val trimmed = title.trim()
        if (trimmed.contains("Featured", ignoreCase = true)) {
            return featuredProducts
        }
        val matchingCategory = allProducts.filter {
            it.category.equals(trimmed, ignoreCase = true)
        }
        return matchingCategory.ifEmpty { allProducts }
    }
}
