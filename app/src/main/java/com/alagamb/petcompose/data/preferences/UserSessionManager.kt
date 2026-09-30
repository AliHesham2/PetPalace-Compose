package com.alagamb.petcompose.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alagamb.petcompose.data.model.user.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

@Singleton
class UserSessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val USER_ID = longPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val userSessionFlow: Flow<User?> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
            if (isLoggedIn) {
                val id = preferences[PreferencesKeys.USER_ID] ?: 0L
                val name = preferences[PreferencesKeys.USER_NAME] ?: ""
                val email = preferences[PreferencesKeys.USER_EMAIL] ?: ""
                User(id = id, username = name, email = email)
            } else {
                null
            }
        }

    val isLoggedInFlow: Flow<Boolean> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    suspend fun saveUserSession(user: User) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = user.id
            preferences[PreferencesKeys.USER_NAME] = user.username
            preferences[PreferencesKeys.USER_EMAIL] = user.email
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
