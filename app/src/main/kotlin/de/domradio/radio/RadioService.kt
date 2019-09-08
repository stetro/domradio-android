package de.domradio.radio

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.DomradioApplication
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber


class RadioService : Service() {

    companion object {
        var isRunning = false
        var state: BehaviorSubject<RadioState> = BehaviorSubject.create()
    }

    private lateinit var notification: Notification
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var focusManager: RadioFocusManager

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            Timber.d("MediaSessionCompat.Callback onPlay() called")
            focusManager.requestAudioFocus()
            mediaPlayer.start()
            mediaSession.setPlaybackState(RadioMediaSession.playingPlaybackState)
            state.onNext(RadioState.PLAYING)
            startForeground(DomradioApplication.NOTIFICATION_ID, notification)
        }

        override fun onStop() {
            Timber.d("MediaSessionCompat.Callback onStop() called")
            focusManager.abandonAudioFocus()
            mediaPlayer.stop()
            mediaSession.setPlaybackState(RadioMediaSession.stoppedPlaybackState)
            state.onNext(RadioState.STOPPED)
            startForeground(DomradioApplication.NOTIFICATION_ID, notification)
            stopSelf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        focusManager = RadioFocusManager(this) {
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                PlaybackStateCompat.ACTION_STOP
            ).send()
        }
        focusManager.requestAudioFocus()
        mediaSession = RadioMediaSession.build(this, mediaSessionCallback)
        mediaPlayer = RadioMediaPlayer.build(this, mediaSession)
        notification = RadioMediaNotification.build(this, mediaSession)

        startForeground(DomradioApplication.NOTIFICATION_ID, notification)

        mediaSession.setPlaybackState(RadioMediaSession.playingPlaybackState)
        state.onNext(RadioState.PLAYING)
    }

    override fun onDestroy() {
        super.onDestroy()
        focusManager.abandonAudioFocus()
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
        return START_STICKY
    }

}