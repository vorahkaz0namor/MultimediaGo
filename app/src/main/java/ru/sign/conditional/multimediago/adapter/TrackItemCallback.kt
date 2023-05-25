package ru.sign.conditional.multimediago.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.sign.conditional.multimediago.dto.Track

class TrackItemCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean =
        oldItem == newItem
}