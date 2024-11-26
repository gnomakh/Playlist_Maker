package com.practicum.playlistmaker.media.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {

    private val favorites: MutableLiveData<Any>? = null
    fun getFavorites(): LiveData<Any>? {
        return favorites
    }

}