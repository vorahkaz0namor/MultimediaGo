package ru.sign.conditional.multimediago.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import ru.sign.conditional.multimediago.R
import ru.sign.conditional.multimediago.databinding.FragmentLectureBinding
import ru.sign.conditional.multimediago.observer.MediaLifecycleObserver
import ru.sign.conditional.multimediago.util.viewBinding

class LectureFragment : Fragment(R.layout.fragment_lecture) {
    private val binding by viewBinding(FragmentLectureBinding::bind)
    private val observer = MediaLifecycleObserver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(observer)
        binding.apply {
            musicPlayButton.setOnClickListener {
//                setLocalDataSource(R.raw.song)
                setNetDataSource(R.string.link_to_play)
            }
            videoPlayButton.setOnClickListener {
                setNetVideo(R.string.dash_to_play)
            }
            stopButton.setOnClickListener {
                observer.mediaPlayer?.stop()
                videoView.stopPlayback()
            }
        }
    }

    // Передача ЛОКАЛЬНОГО файла в MediaPlayer
    private fun setLocalDataSource(@RawRes rawId: Int) {
        observer.apply {
            // Сначала надо получить доступ к файлу
            resources.openRawResourceFd(rawId)
                .use { assetFileDescriptor ->
                    mediaPlayer?.apply {
                        observer.mediaPlayerStateCheck()
                        // Здесь файл передается в MediaPlayer
                        setDataSource(
                            assetFileDescriptor.fileDescriptor,
                            assetFileDescriptor.startOffset,
                            assetFileDescriptor.length
                        )
                    }
                }
        }
            // Запуск воспроизведения
            .play()
    }

    // Передача СЕТЕВОГО файла в MediaPlayer
    private fun setNetDataSource(@StringRes link: Int) {
        observer.apply {
            mediaPlayer?.apply {
                observer.mediaPlayerStateCheck()
                setDataSource(getString(link))
            }
        }
            .play()
    }

    // Передача СЕТЕВОГО файла в VideoView
    private fun setNetVideo(@StringRes link: Int) {
        binding.videoView.apply {
            // Медиаконтроллер для управления воспроизведением
            setMediaController(MediaController(context))
            setVideoURI(
                Uri.parse(getString(link))
            )
            // Чтобы после подготовки началось воспроизведение,
            // необходимо установить слушателя
            setOnPreparedListener { start() }
            // А чтобы по окончании проирывания плеер закрылся,
            // необходимо установить вот такого слушателя
            setOnCompletionListener { stopPlayback() }
        }
    }
}