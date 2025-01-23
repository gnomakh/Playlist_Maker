package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.PlaylistsGetterUseCase
import com.practicum.playlistmaker.media.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.api.RelativityInteractor
import com.practicum.playlistmaker.media.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.media.domain.impl.PlaylistsGetterUseCaseImpl
import com.practicum.playlistmaker.media.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.media.domain.impl.RelativityInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.GetTracksUseCase
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<GetTracksUseCase> {
        GetTracksUseCase(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

    single<PlaylistsGetterUseCase> {
        PlaylistsGetterUseCaseImpl(get())
    }

    single<RelativityInteractor> {
        RelativityInteractorImpl(get())
    }
}
