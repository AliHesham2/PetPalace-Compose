package com.alagamb.petcompose.repo.pet

import com.alagamb.petcompose.data.db.dao.announcement.AnnouncementDao
import com.alagamb.petcompose.data.db.dao.category.CategoryDao
import com.alagamb.petcompose.data.db.dao.pet.PetDao
import com.alagamb.petcompose.data.db.table.pet.PetTable
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.data.model.pet.toDomain
import com.alagamb.petcompose.data.model.pet.toFeaturedDomain
import com.alagamb.petcompose.data.model.pet.toProductItemDomain
import com.alagamb.petcompose.data.model.product.ProductItem
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alagamb.petcompose.data.seed.DatabaseSeeder
import com.alagamb.petcompose.util.ErrorType
import com.alagamb.petcompose.util.ResultCallBack
import com.alagamb.petcompose.util.toFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepositoryImpl @Inject constructor(
    private val databaseSeeder: DatabaseSeeder,
    private val announcementDao: AnnouncementDao,
    private val categoryDao: CategoryDao,
    private val petDao: PetDao
) : PetRepository {

    override suspend fun ensureSeeded() {
        databaseSeeder.seedIfNeeded()
    }

    override fun getAnnouncements(): Flow<List<AnnouncementItem>> {
        return announcementDao.getAllAnnouncements()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getCategories(): Flow<List<PetCategoryItem>> {
        return categoryDao.getAllCategories()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getFeaturedPets(): Flow<List<FeaturedPetItem>> {
        return petDao.getFeaturedPets()
            .map { list -> list.map { it.toFeaturedDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getPetsPaged(
        category: String?,
        tag: String?,
        searchQuery: String?
    ): Flow<PagingData<ProductItem>> {
        val isFeatured = category != null && category.contains("Featured", ignoreCase = true)
        val cleanCategory = if (isFeatured) null else category?.trim()
        return Pager(
            config = PagingConfig(
                pageSize = 6,
                prefetchDistance = 2,
                enablePlaceholders = false
            )
        ) {
            petDao.getPetsPagingSource(
                categoryName = cleanCategory,
                isFeaturedOnly = isFeatured,
                tag = tag?.trim(),
                searchQuery = searchQuery?.trim()
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toProductItemDomain() }
        }
    }

    override fun getPetDetails(petId: String): Flow<ResultCallBack<ProductItem>> {
        return petDao.getPetById(petId).map { entity ->
            if (entity != null) {
                ResultCallBack.Success(entity.toProductItemDomain())
            } else {
                ResultCallBack.Error(
                    type = ErrorType.SERVER,
                    message = "Pet with ID $petId not found"
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun toggleFavorite(petId: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        petDao.updateFavoriteStatus(petId, isFavorite)
    }

    override fun getFilterTags(category: String?): Flow<List<String>> {
        val flow = when {
            category != null && category.contains("Featured", ignoreCase = true) -> {
                petDao.getFeaturedTags()
            }
            category.isNullOrBlank() -> {
                petDao.getAllTags()
            }
            else -> {
                petDao.getTagsByCategory(category.trim())
            }
        }
        return flow.map { tags ->
            listOf("All") + tags.filter { it.isNotBlank() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addPet(pet: PetTable): ResultCallBack<Unit> = withContext(Dispatchers.IO) {
        try {
            petDao.insertPet(pet)
            ResultCallBack.Success(Unit)
        } catch (e: Exception) {
            e.toFailure()
        }
    }
}
