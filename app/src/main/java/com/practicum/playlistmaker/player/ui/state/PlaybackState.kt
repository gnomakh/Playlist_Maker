package com.practicum.playlistmaker.player.ui.state

sealed class PlaybackState {
    object Playing: PlaybackState()
    object Stop: PlaybackState()
    object Pause: PlaybackState()
}