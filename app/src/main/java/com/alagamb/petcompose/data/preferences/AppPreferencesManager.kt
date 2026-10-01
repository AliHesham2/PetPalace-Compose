package com.alagamb.petcompose.data.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferencesManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val IS_DATABASE_SEEDED = booleanPreferencesKey("is_database_seeded")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    val themeModeFlow: Flow<ThemeMode> = context.appDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val modeName = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            try {
                ThemeMode.valueOf(modeName)
            } catch (e: Exception) {
                ThemeMode.SYSTEM
            }
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    val appLanguageFlow: Flow<String> = context.appDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] ?: getInitialLanguage()
        }

    suspend fun setAppLanguage(languageCode: String) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = languageCode
        }
    }

    fun getInitialLanguage(): String {
        val appLocale = AppCompatDelegate.getApplicationLocales()[0]?.language
        if (!appLocale.isNullOrBlank()) {
            return if (appLocale.startsWith("ar", ignoreCase = true)) "ar" else "en"
        }
        val defaultLocale = Locale.getDefault().language
        return if (defaultLocale.startsWith("ar", ignoreCase = true)) "ar" else "en"
    }

    val isDatabaseSeededFlow: Flow<Boolean> = context.appDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_DATABASE_SEEDED] ?: false
        }

    suspend fun setDatabaseSeeded(seeded: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DATABASE_SEEDED] = seeded
        }
    }
}
