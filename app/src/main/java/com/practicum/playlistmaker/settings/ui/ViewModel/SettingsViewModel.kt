package com.practicum.playlistmaker.settings.ui.ViewModel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.model.IntentState
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor()

    private val intentState = MutableLiveData<Intent>()
    fun getIntentState() : LiveData<Intent> {
        return intentState
    }


    fun switchTheme(isChecked: Boolean, application: Application) {
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
            putExtra(Intent.EXTRA_TEXT, getApplication<Application>().getString(R.string.course_link))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intentState.setValue(shareIntent)

    }
    fun openLink() {
        val url = Uri.parse(getApplication<Application>().getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, url)
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intentState.setValue(agreementIntent)
    }

    fun openEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getApplication<Application>().getString(R.string.dev_email)))
            putExtra(Intent.EXTRA_SUBJECT, getApplication<Application>().getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getApplication<Application>().getString(R.string.email_text))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intentState.setValue(supportIntent)
    }
}