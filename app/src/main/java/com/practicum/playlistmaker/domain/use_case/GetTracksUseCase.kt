package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import java.util.concurrent.Executors

class GetTracksUseCase(private val trackRepository: TrackRepository) {

    companion object {
        private const val NO_RESULT = "No result"
        private const val NO_INTERNET = "No internet"
    }

    private val executor = Executors.newSingleThreadExecutor()

    fun execute(expression: String, consumer: TrackConsumer) {
        executor.execute {
            try {
                val response = trackRepository.searchTracks(expression)
                if (response.isNullOrEmpty() == false) {
                    consumer.onSuccess(response)
                } else {
                    consumer.onFailure(NO_RESULT)
                }
            } catch (e : Exception) {
                consumer.onFailure(NO_INTERNET)
            }
        }
    }
}