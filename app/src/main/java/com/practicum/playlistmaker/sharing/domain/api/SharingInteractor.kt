package com.practicum.playlistmaker.sharing.domain.api

import android.content.Intent

interface SharingInteractor {

    fun share() : Intent
    fun sendEmail() : Intent
    fun openTerms() : Intent
}