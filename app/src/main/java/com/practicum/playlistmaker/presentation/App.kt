package com.practicum.playlistmaker.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

class App : Application() {

    var darkTheme = false

    companion object {
        private lateinit var appInstance: App
        fun getContext(): Context {
            return appInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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