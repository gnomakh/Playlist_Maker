package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.RelativityRepositoryImpl
import com.practicum.playlistmaker.media.domain.api.FavoritesRepository
import com.practicum.playlistmaker.media.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.api.RelativityRepository
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.playlist.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.search.data.repository_impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository_impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(sharedPref = get(named("historyPrefs")), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPref = get(named("themePrefs")))
    }

    factory { TrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory { PlaylistDbConverter() }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get())
    }

    single<RelativityRepository> {
        RelativityRepositoryImpl(get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
}

