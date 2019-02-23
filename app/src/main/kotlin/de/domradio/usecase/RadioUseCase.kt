package de.domradio.usecase

import android.content.Context
import android.content.Intent
import android.os.Build
import de.domradio.radio.RadioService
import de.domradio.radio.RadioState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class RadioUseCase(private val context: Context) {


    private val radioStateSubject: PublishSubject<RadioState> = PublishSubject.create()
    private var serviceIntent: Intent = Intent(context, RadioService::class.java)

    fun initialize() {
        Timber.d("initialize() called")
    }

    fun cleanup() {
        Timber.d("cleanup() called")
        context.stopService(serviceIntent)
    }

    fun stopStream() {
        Timber.d("stopStream() called")
        context.stopService(serviceIntent)
    }

    fun startStream() {
        Timber.d("startStream() called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    fun getRadioState(): Observable<RadioState> {
        return radioStateSubject
    }



}
