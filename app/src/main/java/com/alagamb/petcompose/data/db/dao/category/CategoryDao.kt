package com.alagamb.petcompose.data.db.dao.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alagamb.petcompose.data.db.table.category.CategoryTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryTable>)

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<CategoryTable>>

    @Query("SELECT COUNT(*) FROM category_table")
    suspend fun getCategoryCount(): Int
}
