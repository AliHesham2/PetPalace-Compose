package com.alagamb.petcompose.data.seed

import com.alagamb.petcompose.data.db.dao.announcement.AnnouncementDao
import com.alagamb.petcompose.data.db.dao.category.CategoryDao
import com.alagamb.petcompose.data.db.dao.pet.PetDao
import com.alagamb.petcompose.data.preferences.AppPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val appPreferencesManager: AppPreferencesManager,
    private val announcementDao: AnnouncementDao,
    private val categoryDao: CategoryDao,
    private val petDao: PetDao
) {
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val isAlreadySeeded = appPreferencesManager.isDatabaseSeededFlow.first()
        if (!isAlreadySeeded) {
            announcementDao.insertAnnouncements(PetSeedData.announcements)
            categoryDao.insertCategories(PetSeedData.categories)
            petDao.insertPets(PetSeedData.pets)
            appPreferencesManager.setDatabaseSeeded(true)
        }
    }
}
