package com.practicum.playlistmaker.util

import android.annotation.SuppressLint
import android.content.Context
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.repository_impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository_impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.search.domain.impl.GetTracksUseCase
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl

@SuppressLint("StaticFieldLeak")
object Creator {

    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context.applicationContext

    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun providePlayerRepository() : PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideHistoryInteractor(context: Context) : HistoryInteractor {
        return HistoryInteractorImpl(provideHistoryRepository(context))
    }

    private fun provideHistoryRepository(context: Context) : HistoryRepository {
        return HistoryRepositoryImpl(context)
    }

    fun provideGetTracksUseCase() : GetTracksUseCase {
        return GetTracksUseCase(provideTrackRepository())
    }

    private fun provideTrackRepository() : TrackRepository {
        return TrackRepositoryImpl(provideNetworkClient())
    }

    private fun provideNetworkClient() : NetworkClient {
        return RetrofitNetworkClient()
    }

    fun provideSettingsInteractor() : SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }

    private fun provideSettingsRepository() : SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}