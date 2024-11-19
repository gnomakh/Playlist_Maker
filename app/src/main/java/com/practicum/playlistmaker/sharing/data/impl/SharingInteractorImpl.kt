package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.sharing.data.Navigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val navigator: Navigator) : SharingInteractor {

    override fun share() {
        navigator.shareLink()
    }

    override fun sendEmail() {
        navigator.openEmail()
    }

    override fun openTerms() {
        navigator.openLink()
    }
}