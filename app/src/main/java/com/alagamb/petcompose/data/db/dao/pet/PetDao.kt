package com.alagamb.petcompose.data.db.dao.pet

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alagamb.petcompose.data.db.table.pet.PetTable
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(pets: List<PetTable>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetTable)

    /**
     * AndroidX Paging 3 source for pets:
     * - Filters by category (or all if null/blank/"all")
     * - Filters by featured flag
     * - Filters by tag (or all if null/blank/"all")
     * - Searches by query across name, breed, and description
     * - Room generates an optimal LimitOffsetPagingSource automatically
     */
    @Query("""
        SELECT * FROM pet_table 
        WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(:categoryName) = 'all' OR LOWER(category_name) = LOWER(:categoryName))
          AND (:isFeaturedOnly = 0 OR is_featured = 1)
          AND (:tag IS NULL OR :tag = '' OR LOWER(:tag) = 'all' OR LOWER(tag) = LOWER(:tag))
          AND (
              :searchQuery IS NULL OR :searchQuery = '' OR 
              LOWER(name) LIKE '%' || LOWER(:searchQuery) || '%' OR 
              LOWER(description) LIKE '%' || LOWER(:searchQuery) || '%' OR 
              LOWER(breed) LIKE '%' || LOWER(:searchQuery) || '%'
          )
        ORDER BY id ASC
    """)
    fun getPetsPagingSource(
        categoryName: String?,
        isFeaturedOnly: Boolean,
        tag: String?,
        searchQuery: String?
    ): PagingSource<Int, PetTable>

    @Query("""
        SELECT COUNT(*) FROM pet_table 
        WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(:categoryName) = 'all' OR LOWER(category_name) = LOWER(:categoryName))
          AND (:isFeaturedOnly = 0 OR is_featured = 1)
          AND (:tag IS NULL OR :tag = '' OR LOWER(:tag) = 'all' OR LOWER(tag) = LOWER(:tag))
          AND (
              :searchQuery IS NULL OR :searchQuery = '' OR 
              LOWER(name) LIKE '%' || LOWER(:searchQuery) || '%' OR 
              LOWER(description) LIKE '%' || LOWER(:searchQuery) || '%' OR 
              LOWER(breed) LIKE '%' || LOWER(:searchQuery) || '%'
          )
    """)
    suspend fun getPetsCount(
        categoryName: String?,
        isFeaturedOnly: Boolean,
        tag: String?,
        searchQuery: String?
    ): Int

    @Query("SELECT * FROM pet_table WHERE is_featured = 1 ORDER BY id ASC")
    fun getFeaturedPets(): Flow<List<PetTable>>

    @Query("SELECT * FROM pet_table WHERE id = :id LIMIT 1")
    fun getPetById(id: String): Flow<PetTable?>

    @Query("SELECT * FROM pet_table WHERE id = :id LIMIT 1")
    suspend fun findPetById(id: String): PetTable?

    @Query("UPDATE pet_table SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("UPDATE pet_table SET is_favorite = 0")
    suspend fun clearFavorites()

    /**
     * Pets added from the Add Pet screen get a timestamp id (`pet_1759...`), while the seeded
     * ones are named (`pet_dog_1`).
     */
    @Query("DELETE FROM pet_table WHERE id GLOB 'pet_[0-9]*'")
    suspend fun deleteUserAddedPets()

    @Query("SELECT DISTINCT tag FROM pet_table WHERE tag IS NOT NULL AND tag != '' ORDER BY tag ASC")
    fun getAllTags(): Flow<List<String>>

    @Query("SELECT DISTINCT tag FROM pet_table WHERE LOWER(category_name) = LOWER(:categoryName) AND tag IS NOT NULL AND tag != '' ORDER BY tag ASC")
    fun getTagsByCategory(categoryName: String): Flow<List<String>>

    @Query("SELECT DISTINCT tag FROM pet_table WHERE is_featured = 1 AND tag IS NOT NULL AND tag != '' ORDER BY tag ASC")
    fun getFeaturedTags(): Flow<List<String>>
}
