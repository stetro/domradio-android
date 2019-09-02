package de.domradio.radio

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.DomradioApplication
import timber.log.Timber


class RadioService : Service() {

    companion object {
        var isRunning = false
    }

    private lateinit var notification: Notification

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            Timber.d("MediaSessionCompat.Callback onPlay() called")
            mediaPlayer.start()
            mediaSession.setPlaybackState(RadioMediaSession.playingPlaybackState)
            RadioMediaNotification.updateNotification(
                this@RadioService,
                mediaSession,
                RadioMediaNotification.ActionType.PAUSE_ACTION
            )
            startForeground(DomradioApplication.NOTIFICATION_ID, notification)
        }

        override fun onStop() {
            Timber.d("MediaSessionCompat.Callback onStop() called")
            mediaPlayer.stop()
            mediaSession.setPlaybackState(RadioMediaSession.stoppedPlaybackState)
            RadioMediaNotification.updateNotification(
                this@RadioService,
                mediaSession,
                RadioMediaNotification.ActionType.PLAY_ACTION
            )
            startForeground(DomradioApplication.NOTIFICATION_ID, notification)
            stopSelf()
        }

        override fun onPause() {
            Timber.d("MediaSessionCompat.Callback onPause() called")
            mediaPlayer.pause()
            mediaSession.setPlaybackState(RadioMediaSession.pausedPlaybackState)
            RadioMediaNotification.updateNotification(
                this@RadioService,
                mediaSession,
                RadioMediaNotification.ActionType.PLAY_ACTION
            )
            stopForeground(false)
        }
    }

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        mediaSession = RadioMediaSession.build(this, mediaSessionCallback)
        mediaSession.setPlaybackState(RadioMediaSession.playingPlaybackState)
        mediaPlayer = RadioMediaPlayer.build(mediaSession)
        notification = RadioMediaNotification.build(this, mediaSession)
        startForeground(DomradioApplication.NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaSession.release()
        isRunning = false
    }

    override fun onBind(intent: Intent?): RadioServiceBinder {
        return object : RadioServiceBinder() {
            override fun getMediaSession() = mediaSession
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }


}