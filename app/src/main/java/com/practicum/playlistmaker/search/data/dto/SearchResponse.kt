package com.practicum.playlistmaker.search.data.dto

class SearchResponse(val resultCount: Int, val results: ArrayList<TrackDto>) : Response()