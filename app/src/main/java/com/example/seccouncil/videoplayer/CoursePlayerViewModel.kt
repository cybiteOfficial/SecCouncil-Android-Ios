package com.example.seccouncil.videoplayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer


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

// Function to initialize the ExoPlayer and start video playback
    fun initializePlayer(context: Context, videoUri: String) {
        // Check if the player is not initialized or if the video URI has changed
        if (_exoPlayer == null || currentVideoUri != videoUri) {
            releasePlayer()  // Release the old player if it exists, to avoid conflicts and ensure a fresh start

            // Create a new ExoPlayer instance
            _exoPlayer = ExoPlayer.Builder(context).build().apply {
                // Set the media item (the video you want to play) using the provided videoUri
                setMediaItem(MediaItem.fromUri(videoUri))
                prepare()  // Prepare the player to start playing the media item
                seekTo(playbackPosition)  // Set the position where the video should start playing (if resumed)
                playWhenReady = this@VideoPlayerViewModel.playWhenReady  // Control whether the video should start automatically when ready

                // Add a listener to the player to handle playback state changes and errors
                addListener(object : Player.Listener {
                    // This function gets called whenever the playback state changes (e.g., buffering, playing, paused)
                    override fun onPlaybackStateChanged(state: Int) {
                        // If the player is buffering (i.e., loading data), set the 'isBuffering' flag to true
                        isBuffering = (state == Player.STATE_BUFFERING)
                    }

                    // This function handles any errors that occur during playback (e.g., network errors)
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("ExoPlayer", "Playback error: ${error.message}")  // Log the error message for debugging purposes
                    }
                })
            }
            // Save the video URI that has been initialized to avoid unnecessary re-initializations
            currentVideoUri = videoUri
        } else {
            // If the player is already initialized with the same URI, log a message indicating no re-initialization is needed
            Log.d("ExoPlayer", "Player already initialized with the same URI.")
        }
    }


    /**
     * Release the player, saving any playback position or state.
     */
// Function to release the player and clean up resources
    fun releasePlayer() {
        // Check if the ExoPlayer instance is not null
        _exoPlayer?.run {
            // Save the current playback position (where the video was last playing)
            playbackPosition = currentPosition

            // Save whether the player was playing or paused
            playWhenReady = this.playWhenReady

            // Release the ExoPlayer instance, which will stop playback and free up resources
            release()
        }

        // Set the player instance to null, indicating the player is no longer available
        _exoPlayer = null

        // Clear the current video URI, indicating there is no active video
        currentVideoUri = null
    }

    // This is an override function that gets called when the ViewModel is cleared (when the activity or fragment is destroyed)
    override fun onCleared() {
        super.onCleared() // Call the superclass' onCleared function (important for cleaning up)

        // Release the player and clean up resources when the ViewModel is destroyed
        releasePlayer()
    }

}
