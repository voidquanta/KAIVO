package com.kaivo.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "kaivo_settings")

enum class ThemeMode { LIGHT, DARK, SYSTEM }
enum class AppLanguage(val tag: String) { ENGLISH("en"), PERSIAN("fa") }

class SettingsDataStore(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_mode")
    private val languageKey = stringPreferencesKey("app_language")

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        when (prefs[themeKey]) {
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            ThemeMode.DARK.name -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    val language: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
        when (prefs[languageKey]) {
            AppLanguage.PERSIAN.tag -> AppLanguage.PERSIAN
            else -> AppLanguage.ENGLISH
        }
    }

    val onboardingSeen: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[stringPreferencesKey("onboarding_seen")] == "true"
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[themeKey] = mode.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { it[languageKey] = language.tag }
    }

    suspend fun setOnboardingSeen() {
        context.dataStore.edit { it[stringPreferencesKey("onboarding_seen")] = "true" }
    }
}
