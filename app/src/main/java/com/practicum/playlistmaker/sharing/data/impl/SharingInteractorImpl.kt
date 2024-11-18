package com.practicum.playlistmaker.sharing.data.impl

import android.content.Intent
import com.practicum.playlistmaker.sharing.data.Navigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val navigator: Navigator) : SharingInteractor {

    override fun share() : Intent {
        return navigator.shareLink()
    }

    override fun sendEmail() : Intent {
        return navigator.openEmail()
    }

    override fun openTerms() : Intent {
        return navigator.openLink()
    }
}