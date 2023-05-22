package ru.sign.conditional.multimediago.activity

import android.annotation.SuppressLint
import android.media.session.MediaSession
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import ru.sign.conditional.multimediago.databinding.ActivityMainBinding
import ru.sign.conditional.multimediago.observer.ExoMediaLifecycleObserver

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val observer = ExoMediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycle.addObserver(observer)
        binding.exoplayerView.player = observer.player
    }

    @OptIn(UnstableApi::class)
    override fun onResume() {
        super.onResume()
//        hideSystemUI()
        binding.exoplayerView.apply {
            setMediaControllerForExoplayer()
            showController()
        }
    }

    // Full-screen mode
    @SuppressLint("InlinedApi")
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
        WindowInsetsControllerCompat(
            window,
            binding.exoplayerView
        ).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun setMediaControllerForExoplayer() {
        binding.exoplayerView.player.apply {
            val session = MediaSession(
                this@MainActivity,
                "session"
            )
            mediaController = session.controller
            Log.d("MEDIA CONTROLLER",
                        "${mediaController.sessionToken}")
        }
    }
}