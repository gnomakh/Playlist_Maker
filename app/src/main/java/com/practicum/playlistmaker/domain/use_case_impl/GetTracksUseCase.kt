package com.practicum.playlistmaker.domain.use_case_impl

import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.models.Track
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