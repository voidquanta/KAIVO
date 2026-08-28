package com.kaivo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaivo.app.data.AppLanguage
import com.kaivo.app.data.SettingsDataStore
import com.kaivo.app.data.ThemeMode
import com.kaivo.app.util.LocaleHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val store: SettingsDataStore) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = store.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val language: StateFlow<AppLanguage> = store.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.ENGLISH)

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { store.setThemeMode(mode) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { store.setLanguage(language) }
        LocaleHelper.applyLanguage(language)
    }
}

class SettingsViewModelFactory(
    private val store: SettingsDataStore
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(store) as T
    }
}
