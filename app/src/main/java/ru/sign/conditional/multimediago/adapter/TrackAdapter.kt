package ru.sign.conditional.multimediago.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.sign.conditional.multimediago.databinding.CardTrackBinding
import ru.sign.conditional.multimediago.dto.Track

class TrackAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Track, TrackViewHolder>(TrackItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(
            CardTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onInteractionListener
        )

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}