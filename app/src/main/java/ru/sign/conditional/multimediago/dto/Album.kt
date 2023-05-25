package ru.sign.conditional.multimediago.dto

data class Album(
    val id: Long,
    val title: String = "Album",
    val subtitle: String = "",
    val artist: String = "Artist",
    val published: String = "Date",
    val genre: String = "Music",
    val tracks: List<Track> = emptyList()
)
