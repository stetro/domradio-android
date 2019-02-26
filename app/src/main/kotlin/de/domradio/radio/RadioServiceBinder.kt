package de.domradio.radio

import android.os.Binder
import android.support.v4.media.session.MediaSessionCompat

abstract class RadioServiceBinder : Binder() {

    abstract fun getMediaSession(): MediaSessionCompat
}
