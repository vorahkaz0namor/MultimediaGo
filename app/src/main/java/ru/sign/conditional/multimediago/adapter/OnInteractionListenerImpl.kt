package ru.sign.conditional.multimediago.adapter

import ru.sign.conditional.multimediago.dto.Track
import ru.sign.conditional.multimediago.viewmodel.AlbumViewModel

class OnInteractionListenerImpl(
    private val viewModel: AlbumViewModel
) : OnInteractionListener {
    override fun playItem(track: Track) {
        viewModel.playItem(track)
    }
}