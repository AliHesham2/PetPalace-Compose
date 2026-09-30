package com.alagamb.petcompose.repo.pet

import com.alagamb.petcompose.data.db.table.pet.PetTable
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.data.model.product.ProductItem
import androidx.paging.PagingData
import com.alagamb.petcompose.util.ResultCallBack
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun ensureSeeded()
    fun getAnnouncements(): Flow<List<AnnouncementItem>>
    fun getCategories(): Flow<List<PetCategoryItem>>
    fun getFeaturedPets(): Flow<List<FeaturedPetItem>>
    fun getPetsPaged(
        category: String?,
        tag: String? = null,
        searchQuery: String? = null
    ): Flow<PagingData<ProductItem>>
    fun getPetDetails(petId: String): Flow<ResultCallBack<ProductItem>>
    suspend fun toggleFavorite(petId: String, isFavorite: Boolean)
    fun getFilterTags(category: String?): Flow<List<String>>
    suspend fun addPet(pet: PetTable): ResultCallBack<Unit>
}
