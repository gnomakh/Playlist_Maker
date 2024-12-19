package com.practicum.playlistmaker.search.data.repository_impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryRepositoryImpl(
    val sharedPref: SharedPreferences,
    val gSon: Gson
) : HistoryRepository {

    override fun addTrackToHistory(track: Track) {
        val array = getHistory()
        array.removeIf { it.trackId == track.trackId }
        if (array.size > 9) array.removeLast()
        array.add(0, track)

        val conv = gSon.toJson(array)
        sharedPref.edit().putString(HISTORY_KEY, conv)
            .apply()
    }

    override fun getHistory(): ArrayList<Track> {
        val jsonStr = sharedPref.getString(HISTORY_KEY, null) ?: return arrayListOf()
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(jsonStr, listType)
    }

    override fun clearHistory() {
        sharedPref.edit().clear().apply()
    }

    companion object {
        const val HISTORY_KEY = "history_key"
        const val HISTORY_PREFS_KEY = "history_prefs"
    }
}