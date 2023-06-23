package ru.sign.conditional.multimediago.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder
import ru.sign.conditional.multimediago.BuildConfig.BASE_URL
import ru.sign.conditional.multimediago.dto.Album
import ru.sign.conditional.multimediago.dto.Track

class AlbumViewModel : ViewModel() {
    private var _album: MutableLiveData<Album>
    val album: LiveData<Album>
        get() = _album

    init {
        _album = MutableLiveData(createAlbum())
    }

    fun playItem(track: Track) {
        stopItem()
        selectItem(track)
        if (!track.isPlaying) {
            _album.value = _album.value?.let {
                it.copy(
                    tracks = it.tracks.map {
                        if (it.id == track.id)
                            it.copy(isPlaying = true)
                        else
                            it
                    }
                )
            }
        }
        loggingOfTracksIds("PLAY")
    }

    private fun stopItem() {
        _album.value = _album.value?.let {
            it.copy(
                tracks = it.tracks.map {
                    it.copy(isPlaying = false)
                }
            )
        }
        loggingOfTracksIds("STOP")
    }

    private fun selectItem(track: Track) {
        _album.value = _album.value?.let {
            it.copy(
                tracks = it.tracks.map {
                    it.copy(isSelected = track.id == it.id)
                }
            )
        }
    }

    fun isSelectedItem(): Track? =
        _album.value?.let {
            it.tracks.find { track ->
                track.isSelected
            }
        }

    private fun createAlbum() =
        Album(
            id = 1,
            title = "SoundHelix Songs",
            subtitle = "www.soundhelix.com",
            artist = "T.Schürger",
            published = "2009, 2010, 2011, 2013",
            genre = "Electronic",
            tracks = buildList {
                add(Track(
                    id = 0,
                    file = "https://ik.imagekit.io/jwudrxfj5ek/9eb9cfb5-5767-46ba-8135-c26237ab3704._Pk9yQXZ2D.mp3",
                    title = "Track #1"
                ))
                for (i in 2..16) {
                    val track = Track(
                        file = "$BASE_URL$i.mp3",
                        title = "Track #$i"
                    )
                    add(track)
                    val id = indexOf(track)
                    this[id] = track.copy(id = id.toLong())
                }
            }
        )

    private fun loggingOfTracksIds(operation: String) {
        val stats = StringBuilder()
        album.value?.tracks?.map {
            stats.append("${it.title}: id = #${it.id}, isPlaying = ${it.isPlaying}, isSelected = ${it.isSelected}\n")
        }
        Log.d("$operation ALBUM", stats.toString())
    }
}