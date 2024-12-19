package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(val sharedPref: SharedPreferences) : SettingsRepository {

    override fun saveDarkThemeState(themeState: Boolean) {
        sharedPref.edit().putBoolean(THEME_PREFS_KEY, themeState).apply()
    }

    override fun getDarkThemeState(): Boolean {
        return sharedPref.getBoolean(THEME_PREFS_KEY, false)
    }

    override fun getThemeStatePresence() : Boolean {
        return if (sharedPref.contains(THEME_PREFS_KEY)) true
        else false
    }

    companion object {
        const val THEME_PREFS_KEY = "theme_prefs_key"
    }
}