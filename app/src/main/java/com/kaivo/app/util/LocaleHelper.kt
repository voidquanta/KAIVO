package com.kaivo.app.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.kaivo.app.data.AppLanguage
import java.util.Locale

/**
 * Applies the chosen in-app language. On Android 13+ this delegates to the
 * platform's per-app language API. On older versions we wrap the base
 * Context with an updated Configuration/Locale so Compose's layout
 * direction (LTR for English, RTL for Persian) follows automatically.
 */
object LocaleHelper {

    fun applyLanguage(language: AppLanguage) {
        val localeList = LocaleListCompat.forLanguageTags(language.tag)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun wrapContext(context: Context, language: AppLanguage): Context {
        val locale = Locale(language.tag)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    fun isPreTiramisu(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
}
