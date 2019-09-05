package de.domradio.usecase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver
import de.domradio.radio.RadioService
import de.domradio.radio.RadioState
import io.reactivex.Observable
import timber.log.Timber


class RadioUseCase(
    private val context: Context,
    private val radioServiceIntent: Intent,
    private val sharedPreferences: SharedPreferences
) {

    fun stopStream(): Boolean {
        Timber.d("stopStream() called")
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            PlaybackStateCompat.ACTION_STOP
        ).send()
        return true
    }

    fun startStream(): Boolean {
        Timber.d("startStream() called")
        return when {
            checkWifiOnAndConnected() -> startServiceOrStartStream()
            sharedPreferences.getBoolean("cellular_streaming", false) -> startServiceOrStartStream()
            else -> false
        }
    }

    private fun startServiceOrStartStream(): Boolean {
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
        return true
    }

    fun getRadioState(): Observable<RadioState> {
        return RadioService.state
    }

    private fun checkWifiOnAndConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            connectivityManager.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
    }
}
