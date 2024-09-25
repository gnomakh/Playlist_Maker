package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track


class SearchResponse(val results: ArrayList<Track>) : Response()