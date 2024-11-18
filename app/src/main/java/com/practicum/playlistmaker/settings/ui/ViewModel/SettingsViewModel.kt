package com.practicum.playlistmaker.settings.ui.ViewModel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.sharing.domain.model.IntentState
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor()

    private val intentState = MutableLiveData<Intent>()
    fun getIntent() : LiveData<Intent> = intentState

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.saveDarkThemeState(isChecked)
        (getApplication() as App).switchTheme(isChecked)
    }

    fun setIntentType(type: IntentState) {
        when(type) {
            IntentState.SHARE -> intentState.setValue(sharingInteractor.share())
            IntentState.SEND_EMAIL -> intentState.setValue(sharingInteractor.sendEmail())
            IntentState.OPEN_TERMS -> intentState.setValue(sharingInteractor.openTerms())
        }
    }

}