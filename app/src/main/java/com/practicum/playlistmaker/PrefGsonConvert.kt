package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PREFS_KEY = "key_for_prefs"
const val TRACK_KEY = "fact_key"
const val HISTORY_KEY = "history_key"

class PrefGsonConvert(val prefs: SharedPreferences) {

    fun saveTrackToPref(track: Track) {
        val conv = Gson().toJson(track)
        prefs.edit().
        putString(TRACK_KEY, conv)
            .apply()
    }
    fun saveArrToPref(array: ArrayList<Track>, key: String) {
        val conv = Gson().toJson(array)
        prefs.edit().
        putString(key, conv)
            .apply()
    }

    fun getTrackFromPref() : Track? {
        val jsonStr = prefs.getString(TRACK_KEY, null) ?: return null
        return Gson().fromJson(jsonStr, Track::class.java)
    }

    fun getArrFromPref(key: String) : ArrayList<Track>? {
        val jsonStr = prefs.getString(key, null) ?: return null
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        val facts = Gson().fromJson<ArrayList<Track>>(jsonStr, listType)
        return facts
    }
}

