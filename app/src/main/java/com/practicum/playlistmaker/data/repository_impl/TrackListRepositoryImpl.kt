package com.practicum.playlistmaker.data.repository_impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.TrackListRepository

class TrackListRepositoryImpl(override val sharedPreferences: SharedPreferences) : TrackListRepository {

    override fun saveHistory(key: String, array: ArrayList<Track>) {
        val conv = Gson().toJson(array)
        sharedPreferences.edit().
        putString(key, conv)
            .apply()
    }

    override fun getHistory(key: String) : ArrayList<Track>? {
        val jsonStr = sharedPreferences.getString(key, null) ?: return null
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson<ArrayList<Track>>(jsonStr, listType)
    }


}