package de.domradio.radio

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.DomradioApplication
import de.domradio.R
import de.domradio.usecase.data.StationInfo

object RadioMediaNotification {

    private fun stopAction(context: Context) = NotificationCompat.Action(
        R.drawable.ic_stop_white_24dp,
        context.getString(R.string.close),
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP)
    )

    fun build(
        context: Context,
        mediaSession: MediaSessionCompat,
        stationInfo: StationInfo? = null
    ): Notification {
        return NotificationCompat.Builder(context, DomradioApplication.NOTIFICATION_CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_domradio_white)
                priority = NotificationCompat.PRIORITY_MAX

                setContentTitle(context.getString(R.string.live_stream))
                stationInfo?.let {
                    setContentText(it.artist + " - " + it.title)
                }
                setOngoing(false)

                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Enable launching the player by clicking the notification
                setContentIntent(mediaSession.controller?.sessionActivity)

                // Stop the service when the notification is swiped away
                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )

                // Make the transport controls visible on the lock screen
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add an app icon and set its accent color
                // Be careful about the color
                setSmallIcon(R.drawable.ic_domradio_white)
                color = ContextCompat.getColor(context, R.color.colorPrimary)

                // Add a stop button
                addAction(stopAction(context))

                // Take advantage of MediaStyle features
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                        .setShowActionsInCompactView(0)

                        // Add a cancel button
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                context,
                                PlaybackStateCompat.ACTION_STOP
                            )
                        )
                )
            }.build()
    }

    fun updateNotification(
        context: Context,
        mediaSession: MediaSessionCompat,
        stationInfo: StationInfo
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            DomradioApplication.NOTIFICATION_ID,
            build(context, mediaSession, stationInfo)
        )
    }
}