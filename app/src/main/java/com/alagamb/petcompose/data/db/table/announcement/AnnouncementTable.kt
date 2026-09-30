package com.alagamb.petcompose.data.db.table.announcement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcement_table")
data class AnnouncementTable(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "tag")
    val tag: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "cta_text")
    val ctaText: String,

    @ColumnInfo(name = "start_color")
    val startColor: Long,

    @ColumnInfo(name = "end_color")
    val endColor: Long
)
