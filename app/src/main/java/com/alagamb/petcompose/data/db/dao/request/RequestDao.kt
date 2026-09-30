package com.alagamb.petcompose.data.db.dao.request

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alagamb.petcompose.data.db.table.request.RequestTable
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RequestTable)

    @Query("SELECT * FROM request_table ORDER BY created_at DESC")
    fun getAllRequests(): Flow<List<RequestTable>>

    @Query("SELECT * FROM request_table WHERE id = :id LIMIT 1")
    fun getRequestById(id: String): Flow<RequestTable?>

    @Query("DELETE FROM request_table WHERE id = :id")
    suspend fun deleteRequest(id: String)

    @Query("DELETE FROM request_table")
    suspend fun clearAllRequests()
}
