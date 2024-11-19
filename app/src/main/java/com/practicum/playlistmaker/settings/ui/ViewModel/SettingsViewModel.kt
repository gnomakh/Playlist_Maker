package com.practicum.playlistmaker.settings.ui.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.sharing.domain.model.IntentState
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(context: Context): ViewModel() {

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor(context)


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

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(context)
            }
        }
    }

}