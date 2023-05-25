package ru.sign.conditional.multimediago.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import ru.sign.conditional.multimediago.R
import ru.sign.conditional.multimediago.adapter.OnInteractionListenerImpl
import ru.sign.conditional.multimediago.adapter.TrackAdapter
import ru.sign.conditional.multimediago.databinding.ActivityMainBinding
import ru.sign.conditional.multimediago.observer.ExoMediaLifecycleObserver
import ru.sign.conditional.multimediago.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: AlbumViewModel by viewModels()
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: TrackAdapter
    private val observer by lazy {
        ExoMediaLifecycleObserver(viewModel) {
            binding.exoplayerView.player = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        subscribe()
        lifecycle.addObserver(observer)
        setupListeners()
    }

    private fun initViews() {
        adapter = TrackAdapter(OnInteractionListenerImpl(viewModel))
        binding.trackList.root.adapter = adapter
    }

    private fun subscribe() {
        viewModel.album.observe(this) { album ->
            binding.albumDescription.apply {
                albumTitle.text = album.title
                artistName.text = getString(R.string.performer_name, album.artist)
                published.text = getString(R.string.album_published, album.published)
                genre.text = getString(R.string.album_genre, album.genre)
                mainControlButton.isChecked = mainControlButtonIsChecked()
            }
            adapter.submitList(album.tracks)
        }
    }

    private fun setupListeners() {
        binding.albumDescription.mainControlButton.setOnClickListener {
            fun isNotChecked() {
                (it as MaterialButton).isChecked = false
                Toast.makeText(
                    this,
                    getString(R.string.should_select),
                    Toast.LENGTH_LONG
                ).show()
            }
            viewModel.isSelectedItem()?.let {
                viewModel.playItem(it)
            } ?: isNotChecked()
        }
    }

    private fun mainControlButtonIsChecked(): Boolean {
        val selectedItem = viewModel.isSelectedItem()
        return (selectedItem != null && selectedItem.isPlaying)
    }

    // Full-screen mode must be called in onResume()
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
}