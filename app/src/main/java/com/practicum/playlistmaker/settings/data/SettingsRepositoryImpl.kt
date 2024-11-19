package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(PREFS_KEY, Application.MODE_PRIVATE)

    override fun saveDarkThemeState(themeState: Boolean) {
        sharedPreferences.edit().putBoolean(PREFS_KEY, themeState).apply()
    }

    override fun getDarkThemeState() : Boolean {
        return sharedPreferences.getBoolean(PREFS_KEY, false)
    }

    companion object {
        private const val PREFS_KEY = "prefs_key"
    }
}