package com.practicum.playlistmaker.media.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.ui.state.FavoritesState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    init {
        viewModelScope.launch {
            favoritesInteractor.getFavorites().collect { list ->
                if (list.isNullOrEmpty())
                    favorites.postValue(FavoritesState.Empty)
                else
                    favorites.postValue(FavoritesState.Content(list))
            }
        }
    }

    private val favorites = MutableLiveData<FavoritesState>()

    fun getFavorites(): LiveData<FavoritesState> {
        return favorites
    }

}