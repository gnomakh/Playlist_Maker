package com.practicum.playlistmaker.search.data.repository_impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.HistoryRepository

class HistoryRepositoryImpl(
    val sharedPref: SharedPreferences,
    val gSon: Gson) : HistoryRepository {

    override fun addTrackToHistory(array: ArrayList<Track>, track: Track) {

        array.removeIf { it.trackId == track.trackId }
        if(array.size > 9) array.removeLast()
        array.add(0, track)

        val conv = gSon.toJson(array)
        sharedPref.edit().
        putString(HISTORY_KEY, conv)
            .apply()
    }

    override fun getHistory() : ArrayList<Track>? {
        val jsonStr = sharedPref.getString(HISTORY_KEY, null) ?: return null
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson<ArrayList<Track>>(jsonStr, listType)
    }

    override fun clearHistory() {
        sharedPref.edit().clear().apply()
    }

    companion object {
        const val HISTORY_KEY = "history_key"
        const val  HISTORY_PREFS_KEY = "history_prefs"
    }
}