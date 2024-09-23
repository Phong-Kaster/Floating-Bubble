package com.example.jetpack.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

class LanguageUtil(
    private val context: Context
) {
    fun setLanguage(): Context {
//        // Get shared pref from entry point injection
//        val settingsRepository = EntryPointAccessors
//            .fromApplication(context, EntryPointRepository::class.java)
//            .settingRepository()
//
//        // Get saved language
//        val currentLanguage = settingsRepository.getLanguage()
//
//        // Initialize locale
//        val locale: Locale = Locale.forLanguageTag(currentLanguage.code)
//
//        // Wrap context with new locale
//        return withLocale(locale)
        return withLocale(Locale.getDefault())
    }

    @SuppressLint("ObsoleteSdkInt")
    fun withLocale(locale: Locale): Context {
        // Set locale settings
        val config = Configuration()
        // Set new locale language
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            config.setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }
}