package ru.sign.conditional.multimediago.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.sign.conditional.multimediago.R
import ru.sign.conditional.multimediago.databinding.CardTrackBinding
import ru.sign.conditional.multimediago.dto.Track

class TrackViewHolder(
    private val binding: CardTrackBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.apply {
            root.apply {
                setBackgroundColor(context.getColor(
                    if (track.isSelected)
                        R.color.ripple_button
                    else
                        R.color.transparent
                ))
            }
            trackControlButton.isChecked = track.isPlaying
            trackName.text = track.title
            trackLength.text = track.length
        }
        binding.root.setOnClickListener {
            onInteractionListener.playItem(track)
        }
        Log.d("WAS ADDED TRACK", "${track.title}: isChecked = ${binding.trackControlButton.isChecked}")
    }
}