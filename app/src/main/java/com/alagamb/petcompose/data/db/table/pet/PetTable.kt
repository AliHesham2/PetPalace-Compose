package com.alagamb.petcompose.data.db.table.pet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pet_table",
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["category_name"]),
        Index(value = ["is_featured"])
    ]
)
data class PetTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "category_id")
    val categoryId: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String,

    @ColumnInfo(name = "breed")
    val breed: String,

    @ColumnInfo(name = "age")
    val age: String,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "distance")
    val distance: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "rating")
    val rating: Float = 5.0f,

    @ColumnInfo(name = "review_count")
    val reviewCount: Int = 0,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean = false,

    @ColumnInfo(name = "tag")
    val tag: String? = null,

    @ColumnInfo(name = "start_color")
    val startColor: Long,

    @ColumnInfo(name = "end_color")
    val endColor: Long
)
