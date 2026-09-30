package com.alagamb.petcompose.data.db.table.request

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_table")
data class RequestTable(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "pet_id")
    val petId: String,
    @ColumnInfo(name = "pet_name")
    val petName: String,
    @ColumnInfo(name = "pet_category")
    val petCategory: String,
    @ColumnInfo(name = "pet_price")
    val petPrice: String,
    @ColumnInfo(name = "pet_description")
    val petDescription: String,
    @ColumnInfo(name = "tag")
    val tag: String?,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "total_price")
    val totalPrice: String,
    @ColumnInfo(name = "status")
    val status: String = "Pending",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
