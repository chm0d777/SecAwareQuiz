package com.chmod777.secawarequiz.data

import android.content.Context
import android.content.SharedPreferences
import com.chmod777.secawarequiz.ui.theme.ThemePreferences

class ThemeRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(ThemePreferences.PREFS_NAME, Context.MODE_PRIVATE)

    fun saveThemePreference(theme: String) {
        prefs.edit().putString(ThemePreferences.KEY_THEME, theme).apply()
    }

    fun getThemePreference(): String {
        return prefs.getString(ThemePreferences.KEY_THEME, ThemePreferences.THEME_SYSTEM)
            ?: ThemePreferences.THEME_SYSTEM
    }

    fun saveLanguagePreference(languageCode: String) {
        prefs.edit().putString(ThemePreferences.KEY_LANGUAGE, languageCode).apply()
    }

    fun getLanguagePreference(): String {
        return prefs.getString(ThemePreferences.KEY_LANGUAGE, ThemePreferences.LANGUAGE_RUSSIAN)
            ?: ThemePreferences.LANGUAGE_RUSSIAN
    }
}
