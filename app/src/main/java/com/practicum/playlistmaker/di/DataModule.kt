package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.practicum.playlistmaker.media.data.db.favorites_db.AppDatabase
import com.google.gson.Gson
import com.practicum.playlistmaker.media.data.db.playlists_db.PlaylistsDatabase
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesAPI
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository_impl.HistoryRepositoryImpl.Companion.HISTORY_PREFS_KEY
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl.Companion.THEME_PREFS_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single(named("historyPrefs")) {
        androidContext()
            .getSharedPreferences(HISTORY_PREFS_KEY, Context.MODE_PRIVATE)
    }

    single(named("themePrefs")) {
        androidContext()
            .getSharedPreferences(THEME_PREFS_KEY, Context.MODE_PRIVATE)
    }


    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    factory {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favdatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "pldatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
