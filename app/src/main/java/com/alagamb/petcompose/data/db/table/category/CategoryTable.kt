package com.alagamb.petcompose.data.db.table.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon_type")
    val iconType: String,

    @ColumnInfo(name = "background_color")
    val backgroundColor: Long,

    @ColumnInfo(name = "icon_tint_color")
    val iconTintColor: Long
)
