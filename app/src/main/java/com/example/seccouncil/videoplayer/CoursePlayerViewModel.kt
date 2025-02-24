package com.example.seccouncil.videoplayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

/**
 * ViewModel that manages ExoPlayer and playback state (playWhenReady, playback position, buffering).
 */
class VideoPlayerViewModel : ViewModel() {

    private var _exoPlayer: ExoPlayer? = null
    val exoPlayer: ExoPlayer?
        get() = _exoPlayer

    var playbackPosition by mutableStateOf(0L)
        private set
    var playWhenReady by mutableStateOf(true)
        private set
    var isBuffering by mutableStateOf(true)
        private set
    private var currentVideoUri: String? = null // Store the current URI
    /**
     * Initialize the ExoPlayer if not already created and prepare it to play from [videoUri].
     */

    fun initializePlayer(context: Context, videoUri: String) {
        if (_exoPlayer == null || currentVideoUri != videoUri) {
            releasePlayer() // Release the old player to avoid conflicts
            _exoPlayer = ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(videoUri))
                prepare()
                seekTo(playbackPosition)
                playWhenReady = this@VideoPlayerViewModel.playWhenReady

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        isBuffering = (state == Player.STATE_BUFFERING)
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("ExoPlayer", "Playback error: ${error.message}")
                    }
                })
            }
            currentVideoUri = videoUri
        } else {
            Log.d("ExoPlayer", "Player already initialized with the same URI.")
        }
    }

    /**
     * Release the player, saving any playback position or state.
     */
    fun releasePlayer() {
        _exoPlayer?.run {
            playbackPosition = currentPosition
            playWhenReady = this.playWhenReady
            release()
        }
        _exoPlayer = null
        currentVideoUri = null // Clear the current video URI
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
