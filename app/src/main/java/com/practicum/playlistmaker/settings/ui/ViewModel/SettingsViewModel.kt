package com.practicum.playlistmaker.settings.ui.ViewModel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.IntentState
import com.practicum.playlistmaker.util.App

class SettingsViewModel(val application: Application, val settingsInteractor: SettingsInteractor): ViewModel() {

    private val intentState = MutableLiveData<Intent>()
    fun getIntentState() : LiveData<Intent> {
        return intentState
    }

    fun getSwitchState() : Boolean = settingsInteractor.getDarkThemeState()

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.getDarkThemeState()
        settingsInteractor.saveDarkThemeState(isChecked)
        (application as App).switchTheme(isChecked)
    }

    fun setIntentType(type: IntentState) {
        when(type) {
            IntentState.SHARE -> shareLink()
            IntentState.SEND_EMAIL -> openEmail()
            IntentState.OPEN_TERMS -> openLink()
        }
    }

    fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, application.getString(R.string.course_link))
        }
        intentState.setValue(shareIntent)

    }
    fun openLink() {
        val url = Uri.parse(application.getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, url)
        intentState.setValue(agreementIntent)
    }

    fun openEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(application.getString(R.string.dev_email)))
            putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, application.getString(R.string.email_text))
        }
        intentState.setValue(supportIntent)
    }
}