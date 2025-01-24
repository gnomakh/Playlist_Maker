package com.practicum.playlistmaker.media.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.ui.state.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val favoritesLiveData = MutableLiveData<FavoritesState>()

    fun getFavoritesLivedLiveData(): LiveData<FavoritesState> {
        return favoritesLiveData
    }

    fun getFavoritesList() {
        viewModelScope.launch {
            favoritesInteractor.getFavorites().collect { list ->
                if (list.isEmpty())
                    favoritesLiveData.postValue(FavoritesState.Empty)
                else
                    favoritesLiveData.postValue(FavoritesState.Content(list))
            }
        }
    }
}