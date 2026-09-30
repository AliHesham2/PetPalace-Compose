package com.alagamb.petcompose.ui.screens.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem

object DashboardMockData {

    val announcements = listOf(
        AnnouncementItem(
            id = "announcement_1",
            tag = "SPECIAL EVENT",
            title = "Find Your Soul Pet 🐾",
            description = "Over 50+ rescued pets looking for a warm, loving home this weekend.",
            ctaText = "Adopt Now",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF7F53AC),
                    Color(0xFF647DEE)
                )
            )
        ),
        AnnouncementItem(
            id = "announcement_2",
            tag = "FREE HEALTH CAMP",
            title = "Vaccination Drive 💉",
            description = "Complimentary checkups & vaccinations provided by certified veterinarians.",
            ctaText = "View Schedule",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFF758C),
                    Color(0xFFFF7EB3)
                )
            )
        ),
        AnnouncementItem(
            id = "announcement_3",
            tag = "COMMUNITY TIPS",
            title = "New to Pet Care? ✨",
            description = "Read our comprehensive puppy & kitten onboarding guide curated by experts.",
            ctaText = "Read Guide",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF11998E),
                    Color(0xFF38EF7D)
                )
            )
        )
    )

    // Matching user's reference image: Dogs, Cats, Birds, Fish, Others
    val categories = listOf(
        PetCategoryItem(
            id = "dogs",
            name = "Dogs",
            icon = Icons.Default.Pets,
            backgroundColor = Color(0xFFFFE8D6),
            iconTint = Color(0xFF8D4F27)
        ),
        PetCategoryItem(
            id = "cats",
            name = "Cats",
            icon = Icons.Default.Pets,
            backgroundColor = Color(0xFFFFDFC4),
            iconTint = Color(0xFFD4691E)
        ),
        PetCategoryItem(
            id = "birds",
            name = "Birds",
            icon = Icons.Default.FlutterDash,
            backgroundColor = Color(0xFFE2EED8),
            iconTint = Color(0xFF5B8246)
        ),
        PetCategoryItem(
            id = "fish",
            name = "Fish",
            icon = Icons.Default.WaterDrop,
            backgroundColor = Color(0xFFDBE9F6),
            iconTint = Color(0xFF3B729E)
        ),
        PetCategoryItem(
            id = "others",
            name = "Others",
            icon = Icons.Default.CrueltyFree,
            backgroundColor = Color(0xFFFBE0E5),
            iconTint = Color(0xFFB55D6F)
        ),
    )

    val featuredPets = listOf(
        FeaturedPetItem(
            id = "pet_1",
            name = "Milo",
            breed = "Golden Retriever",
            age = "1.5 yrs",
            gender = "Male",
            distance = "2.4 km",
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80))
            )
        ),
        FeaturedPetItem(
            id = "pet_2",
            name = "Luna",
            breed = "British Shorthair",
            age = "8 mos",
            gender = "Female",
            distance = "1.8 km",
            isFavorite = true,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFE1BEE7), Color(0xFFCE93D8))
            )
        ),
        FeaturedPetItem(
            id = "pet_3",
            name = "Charlie",
            breed = "French Bulldog",
            age = "2 yrs",
            gender = "Male",
            distance = "3.1 km",
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFB2EBF2), Color(0xFF80DEEA))
            )
        ),
        FeaturedPetItem(
            id = "pet_4",
            name = "Bella",
            breed = "Persian Cat",
            age = "1 yr",
            gender = "Female",
            distance = "4.0 km",
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFCDD2), Color(0xFFEF9A9A))
            )
        ),
        FeaturedPetItem(
            id = "pet_5",
            name = "Oliver",
            breed = "Holland Lop",
            age = "6 mos",
            gender = "Male",
            distance = "0.9 km",
            isFavorite = false,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFC8E6C9), Color(0xFFA5D6A7))
            )
        ),
        FeaturedPetItem(
            id = "pet_6",
            name = "Daisy",
            breed = "Cockatiel",
            age = "1 yr",
            gender = "Female",
            distance = "5.2 km",
            isFavorite = true,
            cardGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF9C4), Color(0xFFFFF176))
            )
        ),
    )
}
