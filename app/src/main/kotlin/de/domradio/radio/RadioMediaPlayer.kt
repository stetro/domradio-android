package de.domradio.radio

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import timber.log.Timber

object RadioMediaPlayer {

    private const val STREAM_URL_HIGH_QUALITY = "https://dom.audiostream.io/domradio/1000/mp3/128/domradio"
    private const val STREAM_URL_LOW_QUALITY = "https://dom.audiostream.io/domradio/1000/mp3/64/domradio"


    fun build(mediaSession: MediaSessionCompat): MediaPlayer {

        val audioAttributes = AudioAttributes.Builder()
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()

        return MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
            setDataSource(STREAM_URL_LOW_QUALITY)
            setOnErrorListener { mp, what, extra ->
                Timber.d("onError() called with: mp = [$mp], what = [$what], extra = [$extra]")
                mediaSession.setPlaybackState(RadioMediaSession.errorPlayState)
                // TODO: Propagate error message
                return@setOnErrorListener true
            }
            setOnPreparedListener {
                start()
            }
            prepareAsync()
        }

    }
}