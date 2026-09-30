package com.alagamb.petcompose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alagamb.petcompose.data.db.dao.announcement.AnnouncementDao
import com.alagamb.petcompose.data.db.dao.category.CategoryDao
import com.alagamb.petcompose.data.db.dao.pet.PetDao
import com.alagamb.petcompose.data.db.dao.request.RequestDao
import com.alagamb.petcompose.data.db.dao.user.UserDao
import com.alagamb.petcompose.data.db.table.announcement.AnnouncementTable
import com.alagamb.petcompose.data.db.table.category.CategoryTable
import com.alagamb.petcompose.data.db.table.pet.PetTable
import com.alagamb.petcompose.data.db.table.request.RequestTable
import com.alagamb.petcompose.data.db.table.user.UserTable

@Database(
    entities = [
        UserTable::class,
        AnnouncementTable::class,
        CategoryTable::class,
        PetTable::class,
        RequestTable::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun categoryDao(): CategoryDao
    abstract fun petDao(): PetDao
    abstract fun requestDao(): RequestDao
}
