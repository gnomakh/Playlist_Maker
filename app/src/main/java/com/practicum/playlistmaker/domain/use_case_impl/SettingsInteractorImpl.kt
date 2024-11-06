package com.practicum.playlistmaker.domain.use_case_impl

import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.use_case.SettingsInteractor

class SettingsInteractorImpl(val settingsRepo : SettingsRepository) : SettingsInteractor {
    override fun saveDarkThemeState(state: Boolean) {
        settingsRepo.saveDarkThemeState(state)
    }

    override fun getDarkThemeState() : Boolean {
        return settingsRepo.getDarkThemeState()
    }
}