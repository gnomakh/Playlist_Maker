package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class GetTracksUseCase(private val trackRepository: TrackRepository) {

    private val executor = Executors.newSingleThreadExecutor()

    fun execute(expression: String, consumer: TrackConsumer) {
        executor.execute {
            try {
                val foundTracks = trackRepository.searchTracks(expression) as ArrayList<Track>
                if (foundTracks.isNotEmpty()) {
                    consumer.onSuccess(foundTracks)
                } else {
                    consumer.onNoResult()
                }
            } catch (e : Exception) {
                consumer.onNetworkError()
            }
        }
    }
}