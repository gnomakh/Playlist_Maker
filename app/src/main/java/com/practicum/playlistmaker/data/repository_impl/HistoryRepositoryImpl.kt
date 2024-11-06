package com.practicum.playlistmaker.data.repository_impl

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.HistoryRepository

class HistoryRepositoryImpl(context: Context) : HistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(PREFS_KEY, Application.MODE_PRIVATE)
    private val gSon = Gson()

    override fun addTrackToHistory(array: ArrayList<Track>, track: Track) : ArrayList<Track> {
        array.removeIf { it.trackId == track.trackId }
        if(array.size > 9) array.removeLast()
        array.add(0, track)
        return array
    }

    override fun saveHistory(array: ArrayList<Track>) {
        val conv = gSon.toJson(array)
        sharedPreferences.edit().
        putString(HISTORY_KEY, conv)
            .apply()
    }

    override fun getHistory() : ArrayList<Track>? {
        val jsonStr = sharedPreferences.getString(HISTORY_KEY, null) ?: return null
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson<ArrayList<Track>>(jsonStr, listType)
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val HISTORY_KEY = "history_key"
        const val  PREFS_KEY = "shared_preferences_key"
    }
}