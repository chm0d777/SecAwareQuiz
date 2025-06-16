package com.chmod777.secawarequiz.utils

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun getCurrentAppLanguage(): String {
    val locales = AppCompatDelegate.getApplicationLocales()
    if (!locales.isEmpty) {
        return locales[0]?.language ?: Locale.getDefault().language
    }
    return Locale.getDefault().language
}

fun setAppLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    val appLocale = LocaleListCompat.create(locale)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
