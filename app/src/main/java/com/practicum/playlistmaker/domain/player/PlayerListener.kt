package com.practicum.playlistmaker.domain.player

interface PlayerListener {
    fun onPlayerStop()
    fun onPlayerStart()
    fun onPlayerPause()
}