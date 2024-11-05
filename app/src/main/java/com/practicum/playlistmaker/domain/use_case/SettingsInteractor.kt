package com.practicum.playlistmaker.domain.use_case

interface SettingsInteractor {
    fun saveDarkThemeState(state: Boolean)
    fun getDarkThemeState() : Boolean
}