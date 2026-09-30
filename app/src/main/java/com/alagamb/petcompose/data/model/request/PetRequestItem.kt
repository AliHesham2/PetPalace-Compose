package com.alagamb.petcompose.data.model.request

import androidx.compose.ui.graphics.Brush
import com.alagamb.petcompose.data.db.table.request.RequestTable
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PetRequestItem(
    val id: String,
    val petId: String,
    val petName: String,
    val petCategory: String,
    val petPrice: String,
    val petDescription: String,
    val tag: String?,
    val quantity: Int,
    val totalPrice: String,
    val status: String,
    val createdAt: Long
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            return sdf.format(Date(createdAt))
        }

    val cardGradient: Brush
        get() = when (petCategory.lowercase(Locale.ROOT)) {
            "dogs" -> Brush.horizontalGradient(AppColors.Gradient.PurpleTeal)
            "cats" -> Brush.horizontalGradient(AppColors.Gradient.SunsetWarm)
            "birds" -> Brush.horizontalGradient(AppColors.Gradient.GreenMint)
            "fish" -> Brush.horizontalGradient(AppColors.Gradient.OceanDeep)
            else -> Brush.horizontalGradient(AppColors.Gradient.RoseGold)
        }
}

fun RequestTable.toDomain(): PetRequestItem = PetRequestItem(
    id = id,
    petId = petId,
    petName = petName,
    petCategory = petCategory,
    petPrice = petPrice,
    petDescription = petDescription,
    tag = tag,
    quantity = quantity,
    totalPrice = totalPrice,
    status = status,
    createdAt = createdAt
)
