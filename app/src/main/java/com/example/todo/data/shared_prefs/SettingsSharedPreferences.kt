package com.example.todo.data.shared_prefs

import android.content.Context
import androidx.core.content.edit

class SettingsSharedPreferences(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)

    fun getTheme(): String =
        sharedPreferences.getString(KEY_THEME, LIGHT_THEME) ?: LIGHT_THEME

    fun setTheme(mode: String) {
        sharedPreferences.edit { putString(KEY_THEME, mode) }
    }

    companion object {
        private const val SETTINGS_PREFS_NAME = "settings"
        private const val KEY_THEME = "theme"
        const val ENGLISH_LANGUAGE_CODE = "en"
        const val ARABIC_LANGUAGE_CODE = "ar"
        const val LIGHT_THEME = "Light"
    }
}
