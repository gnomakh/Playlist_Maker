package com.practicum.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun getThemeStatePresence() : Boolean
    fun saveDarkThemeState(state: Boolean)
    fun getDarkThemeState(): Boolean
}