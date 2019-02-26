package de.domradio.radio

import android.content.Context
import android.content.Intent
import androidx.media.session.MediaButtonReceiver
import timber.log.Timber

class RadioMediaButtonReceiver : MediaButtonReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null
            || Intent.ACTION_MEDIA_BUTTON != intent.action
            || !intent.hasExtra(Intent.EXTRA_KEY_EVENT)
        ) {
            Timber.d("Ignore unsupported intent: $intent")
            return
        }
        Timber.d("onReceive() called with: context = [$context], intent = [$intent] extras=[${intent.extras}]")
        super.onReceive(context, intent)
    }
}
