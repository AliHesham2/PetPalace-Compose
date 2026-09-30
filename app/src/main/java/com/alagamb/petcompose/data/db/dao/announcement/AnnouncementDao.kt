package com.alagamb.petcompose.data.db.dao.announcement

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alagamb.petcompose.data.db.table.announcement.AnnouncementTable
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementTable>)

    @Query("SELECT * FROM announcement_table")
    fun getAllAnnouncements(): Flow<List<AnnouncementTable>>

    @Query("SELECT COUNT(*) FROM announcement_table")
    suspend fun getAnnouncementCount(): Int
}
