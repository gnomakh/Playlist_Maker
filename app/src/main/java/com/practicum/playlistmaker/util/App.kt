package com.practicum.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Creator.initialize(this.applicationContext)
        darkTheme = Creator.provideSettingsInteractor().getDarkThemeState()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}