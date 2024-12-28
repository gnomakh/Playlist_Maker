package com.practicum.playlistmaker.search.data.repository_impl

import android.content.SharedPreferences
import com.example.courutines.db.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val sharedPref: SharedPreferences,
    private val gSon: Gson,
    private val db: AppDatabase
) : HistoryRepository {

    override suspend fun addTrackToHistory(track: Track) {
        val jsonStr = sharedPref.getString(HISTORY_KEY, null)
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        val array = mapFavorites(Gson().fromJson<ArrayList<Track>>(jsonStr, listType) ?: arrayListOf())
        array.removeIf { it.trackId == track.trackId }
        if (array.size > 9) array.removeLast()
        array.add(0, track)

        val conv = gSon.toJson(array)
        sharedPref.edit().putString(HISTORY_KEY, conv)
            .apply()
    }

    override fun getHistory(): Flow<List<Track>> = flow {
        val jsonStr = sharedPref.getString(HISTORY_KEY, null)
        val listType = object : TypeToken<List<Track>>() {}.type
        emit(mapFavorites(Gson().fromJson(jsonStr, listType) ?: arrayListOf()))
    }

    override fun clearHistory() {
        sharedPref.edit().clear().apply()
    }

    private suspend fun mapFavorites(tracks: ArrayList<Track>) : ArrayList<Track> {
            val ids = db.getTrackDao().getTracksId()
            return tracks.map {
                track ->
                    track.apply {
                        isFavorite =
                            track.trackId in ids
                    }
            } as ArrayList<Track>
    }

    companion object {
        const val HISTORY_KEY = "history_key"
        const val HISTORY_PREFS_KEY = "history_prefs"
    }
}