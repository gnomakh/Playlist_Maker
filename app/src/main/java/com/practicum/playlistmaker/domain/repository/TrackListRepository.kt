package com.practicum.playlistmaker.domain.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.models.Track

interface TrackListRepository {
    val sharedPreferences: SharedPreferences
    fun saveHistory(key: String, array: ArrayList<Track>,)
    fun getHistory(key: String) : ArrayList<Track>?
}