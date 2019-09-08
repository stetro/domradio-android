package de.domradio.radio

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import de.domradio.R
import de.domradio.ui.MainActivity

object RadioMediaSession {

    val stoppedPlaybackState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_STOPPED, 0, 0.0f)
        .setActions(PlaybackStateCompat.ACTION_PLAY)
        .build()

    val playingPlaybackState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
        .setActions(PlaybackStateCompat.ACTION_STOP)
        .build()

    val errorPlayState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_ERROR, 0, 1.0f)
        .build()

    fun build(context: Context, mediaSessionCallback: MediaSessionCompat.Callback): MediaSessionCompat {

        val componentName = ComponentName(context, RadioMediaButtonReceiver::class.java)

        val metadata = MediaMetadataCompat.Builder()
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                BitmapFactory.decodeResource(context.resources, R.raw.domradio)
            )
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON,
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_domradio_white)
            )
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, context.getString(R.string.live_stream))
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, context.getString(R.string.app_name))
            .build()

        val mainActivityPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return MediaSessionCompat(context, "RadioMediaSession", componentName, null)
            .apply {
                setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
                setCallback(mediaSessionCallback)
                isActive = true
                setMetadata(metadata)
                setSessionActivity(mainActivityPendingIntent)
            }
    }
}