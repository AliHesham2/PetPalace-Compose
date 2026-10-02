package com.alagamb.petcompose.repo.user

import com.alagamb.petcompose.data.model.user.User
import com.alagamb.petcompose.util.ResultCallBack
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<User?>
    fun register(username: String, email: String, password: String): Flow<ResultCallBack<User>>
    fun login(email: String, password: String): Flow<ResultCallBack<User>>
    fun getUserByEmail(email: String): Flow<User?>
    fun getAllUsers(): Flow<List<User>>
    suspend fun logout()

    /** Deletes the signed-in account and everything it created on this device. */
    suspend fun deleteAccount()
}