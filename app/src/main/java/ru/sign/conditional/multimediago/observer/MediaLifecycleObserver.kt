package ru.sign.conditional.multimediago.observer

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MediaLifecycleObserver : LifecycleEventObserver {
    // Переменная, с помощью которой будет изменяться состояние
    // MediaPlayer'а в зависимости от жизненного цикла Activity
    var mediaPlayer: MediaPlayer? = MediaPlayer()

    // Функция, которая будет запускать воспроизведение
    fun play() {
        mediaPlayer?.apply {
            setOnPreparedListener { start() }
            prepareAsync()
        }
    }

    fun mediaPlayerStateCheck() {
        mediaPlayer?.reset()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START ->
                if (mediaPlayer == null)
                    mediaPlayer = MediaPlayer()
            Lifecycle.Event.ON_PAUSE -> mediaPlayer?.pause()
            Lifecycle.Event.ON_STOP -> {
                mediaPlayer?.release()
                mediaPlayer = null
            }
            Lifecycle.Event.ON_DESTROY ->
                source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}