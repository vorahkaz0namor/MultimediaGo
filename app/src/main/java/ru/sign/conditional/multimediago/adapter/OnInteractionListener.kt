package ru.sign.conditional.multimediago.adapter

import ru.sign.conditional.multimediago.dto.Track

interface OnInteractionListener {
    fun playItem(track: Track)
}