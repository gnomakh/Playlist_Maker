package com.practicum.playlistmaker.domain.consumer

import com.practicum.playlistmaker.domain.models.Track

sealed interface ConsumerData<T> {
    data class Data<T>(val result: List<Track>) : ConsumerData<T>
    data class Error<T>(val message: String) : ConsumerData<T>
}