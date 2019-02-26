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

object RadioMediaNotification {

    enum class ActionType {
        PAUSE_ACTION, PLAY_ACTION
    }

    private fun stopAction(context: Context) = NotificationCompat.Action(
        R.drawable.ic_close_white24dp,
        context.getString(R.string.close),
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP)
    )

    private fun playAction(context: Context) = NotificationCompat.Action(
        R.drawable.ic_play_arrow_white_24dp,
        context.getString(R.string.play),
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY)
    )

    private fun pauseAction(context: Context) = NotificationCompat.Action(
        R.drawable.ic_pause_white_24dp,
        context.getString(R.string.pause),
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PAUSE)
    )

    private fun build(
        context: Context,
        mediaSession: MediaSessionCompat,
        action: NotificationCompat.Action
    ): Notification {
        return NotificationCompat.Builder(context, DomradioApplication.NOTIFICATION_CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_domradio_white)
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.raw.domradio))
                priority = NotificationCompat.PRIORITY_MAX

                setContentTitle("DOMRADIO.DE")
                setOngoing(false)
                setContentInfo("Livestream")

                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
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

                // Add the current action button
                addAction(action)
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

    fun build(
        context: Context,
        mediaSession: MediaSessionCompat
    ): Notification = RadioMediaNotification.build(context, mediaSession, pauseAction(context))

    fun updateNotification(context: Context, mediaSession: MediaSessionCompat, actionType: ActionType) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val action = when (actionType) {
            RadioMediaNotification.ActionType.PAUSE_ACTION -> pauseAction(context)
            RadioMediaNotification.ActionType.PLAY_ACTION -> playAction(context)
        }
        notificationManager.notify(DomradioApplication.NOTIFICATION_ID, build(context, mediaSession, action))
    }
}