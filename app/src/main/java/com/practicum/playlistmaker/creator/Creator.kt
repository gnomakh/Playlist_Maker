package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.domain.use_case.ClearEditTextUseCase

object Creator {

    fun provideClearEditTextUseCase() : ClearEditTextUseCase {
        return ClearEditTextUseCase()
    }

}