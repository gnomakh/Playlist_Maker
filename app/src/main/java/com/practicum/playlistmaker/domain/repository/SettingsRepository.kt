package com.practicum.playlistmaker.domain.repository

interface SettingsRepository {
    fun saveDarkThemeState(themeState: Boolean)
    fun getDarkThemeState() : Boolean
}