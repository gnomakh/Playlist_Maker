package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.repository_impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository_impl.TrackListRepositoryImpl
import com.practicum.playlistmaker.domain.repository.TrackListRepository
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.use_case.GetTracksUseCase
import com.practicum.playlistmaker.domain.use_case.TrackListInteractor

object Creator {

    fun provideTrackListInteractor(sharedPreferences: SharedPreferences) : TrackListInteractor {
        return TrackListInteractor(TrackListRepositoryImpl(sharedPreferences))
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
}