package ru.sign.conditional.multimediago.observer

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import ru.sign.conditional.multimediago.R

class ExoMediaLifecycleObserver : LifecycleEventObserver {
    private var _player: ExoPlayer? = null
    val player
        get() = _player
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> initializePlayer(source)
            Lifecycle.Event.ON_RESUME ->
                if (player == null)
                    initializePlayer(source)
            Lifecycle.Event.ON_STOP -> releasePlayer()
            Lifecycle.Event.ON_DESTROY ->
                source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }

    private fun initializePlayer(source: LifecycleOwner) {
        _player = ExoPlayer.Builder(source as Context)
            .build()
            .also {
                val mediaItem = MediaItem
                    .fromUri(source.getString(R.string.video_link_to_play))
                it.setMediaItems(listOf(mediaItem), mediaItemIndex, playbackPosition)
                it.playWhenReady = playWhenReady
                it.prepare()
            }
    }

    private fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            mediaItemIndex = it.currentMediaItemIndex
            playWhenReady = it.playWhenReady
            it.release()
        }
        _player = null
    }
}