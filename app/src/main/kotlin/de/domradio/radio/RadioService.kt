package de.domradio.radio

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.DomradioApplication
import timber.log.Timber


class RadioService : Service() {
    private lateinit var notification: Notification


    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        notification = NotificationCompat.Builder(this, DomradioApplication.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(de.domradio.R.drawable.ic_stat_domradio)
            .setLargeIcon(BitmapFactory.decodeResource(resources, de.domradio.R.mipmap.ic_launcher))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle("DOMRADIO.DE")
            .setContentInfo("Livestream")
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startForeground(DomradioApplication.NOTIFICATION_ID, notification)

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            )
            setDataSource(STREAM_URL_LOW_QUALITY)
            setOnPreparedListener {
                Timber.d("start stream")
                mediaPlayer.start()
            }
            setOnErrorListener { mp, what, extra ->
                Timber.d("onError() called with: mp = [$mp], what = [$what], extra = [$extra]")
                return@setOnErrorListener true
            }
            prepareAsync()
        }



    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val STREAM_URL_HIGH_QUALITY = "https://dom.audiostream.io/domradio/1000/mp3/128/domradio"
        const val STREAM_URL_LOW_QUALITY = "https://dom.audiostream.io/domradio/1000/mp3/64/domradio"
    }
}