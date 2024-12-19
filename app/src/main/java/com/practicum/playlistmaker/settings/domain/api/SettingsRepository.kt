package com.practicum.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun getThemeStatePresence() : Boolean
    fun saveDarkThemeState(themeState: Boolean)
    fun getDarkThemeState(): Boolean
}