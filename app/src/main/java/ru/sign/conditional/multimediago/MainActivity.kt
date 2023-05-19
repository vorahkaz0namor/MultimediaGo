package ru.sign.conditional.multimediago

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import ru.sign.conditional.multimediago.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val observer = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycle.addObserver(observer)
        binding.apply {
            musicPlayButton.setOnClickListener {
//                setLocalDataSource(R.raw.song)
                setNetDataSource(R.string.link_to_play)
            }
            videoPlayButton.setOnClickListener {
                setNetVideo(R.string.video_link_to_play)
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
            setMediaController(MediaController(this@MainActivity))
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