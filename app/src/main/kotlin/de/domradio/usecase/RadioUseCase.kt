package de.domradio.usecase

import android.content.Context
import android.content.Intent
import android.os.Build
import de.domradio.radio.RadioState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class RadioUseCase(private val context: Context, private val radioServiceIntent: Intent) {

    private val radioStateSubject: PublishSubject<RadioState> = PublishSubject.create()

    fun initialize() {
        Timber.d("initialize() called")
    }

    fun cleanup() {
        Timber.d("cleanup() called")

    }

    fun stopStream() {
        Timber.d("stopStream() called")
    }

    fun startStream() {
        Timber.d("startStream() called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(radioServiceIntent)
        } else {
            context.startService(radioServiceIntent)
        }
    }

    fun getRadioState(): Observable<RadioState> {
        return radioStateSubject
    }

}
