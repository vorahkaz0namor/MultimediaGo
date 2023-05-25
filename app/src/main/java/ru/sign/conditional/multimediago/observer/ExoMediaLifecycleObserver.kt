package ru.sign.conditional.multimediago.observer

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import ru.sign.conditional.multimediago.viewmodel.AlbumViewModel

private const val EXOPLAYER_TAG = "EXOPLAYER"

class ExoMediaLifecycleObserver(
    private val viewModel: AlbumViewModel,
    private val onPlayerReady: (ExoPlayer) -> Unit
) : LifecycleEventObserver {
    private var player: ExoPlayer? = null
    private lateinit var mediaItems: List<MediaItem>
    private var playWhenReady = false
    private var playbackMediaItemIndex = 0
    private var playbackPosition = 0L
    private val playbackStateListener: Player.Listener = playbackStateListener()

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
        player = ExoPlayer.Builder(source as Context)
            .build()
            .also {
                mediaItems = viewModel.album.value?.tracks?.map { track ->
                    MediaItem.fromUri(track.file)
                } ?: emptyList()
                // Set adaptive track selection
                it.trackSelectionParameters = it.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()
                it.setMediaItems(mediaItems, playbackMediaItemIndex, playbackPosition)
                it.repeatMode = Player.REPEAT_MODE_ALL
                it.playWhenReady = playWhenReady
                it.addListener(playbackStateListener)
                setTrackObserver(source)
                it.prepare()
                // Передача созданного плеера
                onPlayerReady(it)
            }
    }

    private fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            playbackMediaItemIndex = it.currentMediaItemIndex
            playWhenReady = it.playWhenReady
            it.removeListener(playbackStateListener)
            it.release()
        }
        player = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val currentState: String =
                when (playbackState) {
                    ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
                    ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
                    ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
                    ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
                    else -> "UNKNOWN STATE of ExoPlayer"
                }
            Log.d(EXOPLAYER_TAG, "has changed state to $currentState")
        }
    }

    private fun setTrackObserver(source: LifecycleOwner) {
        viewModel.album.observe(source) {
            it.tracks.find {
                it.isPlaying
            }?.let { foundTrack ->
                mediaItems.find { item ->
                    item.localConfiguration?.uri.toString() == foundTrack.file
                }?.let { foundItem ->
                    val index = mediaItems.indexOf(foundItem)
                    player?.apply {
                        if (currentMediaItemIndex == index)
                            play()
                        else {
                            seekToDefaultPosition(index)
                            playWhenReady = true
                            prepare()
                        }
                    }
                    Log.d("WAS FOUND ITEM", "MediaItem #$index")
                }
            } ?: player?.apply {
                pause()
            }
        }
    }
}