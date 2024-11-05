package com.practicum.playlistmaker.data.dto

class SearchResponse(val resultCount: Int, val results: ArrayList<TrackDto>) : Response()