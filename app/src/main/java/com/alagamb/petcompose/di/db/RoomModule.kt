package com.alagamb.petcompose.di.db

import android.content.Context
import androidx.room.Room
import com.alagamb.petcompose.data.db.AppDatabase
import com.alagamb.petcompose.data.db.dao.announcement.AnnouncementDao
import com.alagamb.petcompose.data.db.dao.category.CategoryDao
import com.alagamb.petcompose.data.db.dao.pet.PetDao
import com.alagamb.petcompose.data.db.dao.request.RequestDao
import com.alagamb.petcompose.data.db.dao.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideAnnouncementDao(db: AppDatabase): AnnouncementDao {
        return db.announcementDao()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }

    @Provides
    fun providePetDao(db: AppDatabase): PetDao {
        return db.petDao()
    }

    @Provides
    fun provideRequestDao(db: AppDatabase): RequestDao {
        return db.requestDao()
    }
}