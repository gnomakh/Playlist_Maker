package com.practicum.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun saveDarkThemeState(state: Boolean)
    fun getDarkThemeState() : Boolean
}