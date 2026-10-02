package com.alagamb.petcompose.repo.user

import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.db.dao.pet.PetDao
import com.alagamb.petcompose.data.db.dao.request.RequestDao
import com.alagamb.petcompose.data.db.dao.user.UserDao
import com.alagamb.petcompose.data.db.table.user.UserTable
import com.alagamb.petcompose.data.model.user.User
import com.alagamb.petcompose.data.model.user.toDomain
import com.alagamb.petcompose.data.security.PasswordHasher
import com.alagamb.petcompose.util.ErrorType
import com.alagamb.petcompose.util.ResultCallBack
import com.alagamb.petcompose.util.toFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.alagamb.petcompose.data.preferences.UserSessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val petDao: PetDao,
    private val requestDao: RequestDao,
    private val userSessionManager: UserSessionManager
) : UserRepository {

    override val currentUser: Flow<User?> = userSessionManager.userSessionFlow

    override fun register(
        username: String,
        email: String,
        password: String
    ): Flow<ResultCallBack<User>> = flow {
        try {
            // Check if email already exists in DB
            if (userDao.findUserByEmail(email) != null) {
                emit(
                    ResultCallBack.Error(
                        type = ErrorType.SERVER,
                        messageRes = R.string.error_email_already_registered
                    )
                )
                return@flow
            }

            // Insert new user into database, the password is never stored as plain text
            val userEntity = UserTable(
                username = username,
                email = email,
                password = withContext(Dispatchers.Default) { PasswordHasher.hash(password) }
            )
            val generatedId = userDao.insertUser(userEntity)
            val user = User(
                id = generatedId,
                username = username,
                email = email
            )
            // Persist session to DataStore
            userSessionManager.saveUserSession(user)
            emit(ResultCallBack.Success(user))
        } catch (e: Exception) {
            emit(e.toFailure())
        }
    }

    override fun login(
        email: String,
        password: String
    ): Flow<ResultCallBack<User>> = flow {
        try {
            val user = userDao.findUserByEmail(email)
            when {
                user == null -> {
                    emit(
                        ResultCallBack.Error(
                            type = ErrorType.SERVER,
                            messageRes = R.string.error_user_not_found
                        )
                    )
                }
                !isPasswordCorrect(user, password) -> {
                    emit(
                        ResultCallBack.Error(
                            type = ErrorType.SERVER,
                            messageRes = R.string.error_incorrect_password
                        )
                    )
                }
                else -> {
                    val domainUser = user.toDomain()
                    // Persist session to DataStore
                    userSessionManager.saveUserSession(domainUser)
                    emit(ResultCallBack.Success(domainUser))
                }
            }
        } catch (e: Exception) {
            emit(e.toFailure())
        }
    }

    override fun getUserByEmail(email: String): Flow<User?> {
        return userDao.getUserByEmail(email).map { it?.toDomain() }
    }

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { users -> users.map { it.toDomain() } }
    }

    override suspend fun logout() {
        userSessionManager.clearSession()
    }

    override suspend fun deleteAccount() {
        val user = userSessionManager.userSessionFlow.first() ?: return
        // Requests, favorites and added pets are not tied to an account in the database:
        // they all belong to whoever uses the app on this device.
        requestDao.clearAllRequests()
        petDao.clearFavorites()
        petDao.deleteUserAddedPets()
        userDao.deleteUserByEmail(user.email)
        userSessionManager.clearSession()
    }

    private suspend fun isPasswordCorrect(user: UserTable, password: String): Boolean {
        if (PasswordHasher.isHashed(user.password)) {
            return withContext(Dispatchers.Default) { PasswordHasher.verify(password, user.password) }
        }
        // Account created before passwords were hashed: upgrade it on its first successful login
        if (user.password != password) return false
        val hashed = withContext(Dispatchers.Default) { PasswordHasher.hash(password) }
        userDao.updatePassword(user.id, hashed)
        return true
    }
}