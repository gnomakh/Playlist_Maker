package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(val settingsRepo: SettingsRepository) : SettingsInteractor {
    override fun saveDarkThemeState(state: Boolean) {
        settingsRepo.saveDarkThemeState(state)
    }

    override fun getDarkThemeState(): Boolean {
        return settingsRepo.getDarkThemeState()
    }
}