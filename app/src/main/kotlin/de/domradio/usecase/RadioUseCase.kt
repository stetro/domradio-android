package de.domradio.usecase

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.radio.RadioService
import de.domradio.radio.RadioState
import io.reactivex.Observable
import timber.log.Timber


class RadioUseCase(private val context: Context, private val radioServiceIntent: Intent) {

    fun initialize() {
        Timber.d("initialize() called")
    }

    fun cleanup() {
        Timber.d("cleanup() called")

    }

    fun stopStream() {
        Timber.d("stopStream() called")
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_STOP
        ).send()
    }

    fun startStream() {
        Timber.d("startStream() called")
        if (!RadioService.isRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(radioServiceIntent)
            } else {
                context.startService(radioServiceIntent)
            }
        } else {
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_PLAY
            ).send()
        }
    }

    fun getRadioState(): Observable<RadioState> {
        return RadioService.state
    }

}
