package com.practicum.playlistmaker.di

import android.app.Application
import com.practicum.playlistmaker.media.ui.ViewModel.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.ViewModel.PlaylistCreationViewModel
import com.practicum.playlistmaker.media.ui.ViewModel.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.ViewModel.PlayerViewModel
import com.practicum.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.ViewModel.SearchViewModel
import com.practicum.playlistmaker.settings.ui.ViewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(androidContext() as Application, get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        PlaylistCreationViewModel(get(), get(), get(), get())
    }

    viewModel {
        PlaylistViewModel(get(), get(), get())
    }
}

