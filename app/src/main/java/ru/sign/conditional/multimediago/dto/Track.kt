package ru.sign.conditional.multimediago.dto

data class Track(
    val id: Long = 0,
    val file: String,
    val isPlaying: Boolean = false,
    val isSelected: Boolean = false,
    val title: String = "Track #$id",
    val length: String = ""
)
