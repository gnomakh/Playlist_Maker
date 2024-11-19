package com.practicum.playlistmaker.settings.ui.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.sharing.domain.model.IntentState
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel: ViewModel() {

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor()


    fun switchTheme(isChecked: Boolean, application: Application) {
        settingsInteractor.saveDarkThemeState(isChecked)
        (application as App).switchTheme(isChecked)
    }

    fun setIntentType(type: IntentState) {
        when(type) {
            IntentState.SHARE -> sharingInteractor.share()
            IntentState.SEND_EMAIL -> sharingInteractor.sendEmail()
            IntentState.OPEN_TERMS -> sharingInteractor.openTerms()
        }
    }
}