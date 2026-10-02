package com.alagamb.petcompose.data.db.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alagamb.petcompose.data.db.table.user.UserTable
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserTable): Long

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<UserTable?>

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): UserTable?

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<UserTable>>

    @Query("UPDATE user_table SET password = :password WHERE id = :id")
    suspend fun updatePassword(id: Long, password: String)

    @Query("DELETE FROM user_table WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)
}