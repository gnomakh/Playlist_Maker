package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.presentation.App
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.repository_impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository_impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository_impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository_impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.repository.PlayerRepository
import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.use_case.HistoryInteractor
import com.practicum.playlistmaker.domain.use_case.PlayerInteractor
import com.practicum.playlistmaker.domain.use_case.SettingsInteractor
import com.practicum.playlistmaker.domain.use_case_impl.GetTracksUseCase
import com.practicum.playlistmaker.domain.use_case_impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.use_case_impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.use_case_impl.SettingsInteractorImpl

object Creator {

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun providePlayerRepository() : PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideHistoryInteractor() : HistoryInteractor {
        return HistoryInteractorImpl(provideHistoryRepository())
    }

    private fun provideHistoryRepository() : HistoryRepository {
        return HistoryRepositoryImpl(App.getContext())
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

    private fun provideSettingsRepository() : SettingsRepository{
        return SettingsRepositoryImpl(App.getContext())
    }
}