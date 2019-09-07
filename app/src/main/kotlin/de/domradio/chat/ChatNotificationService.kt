package de.domradio.chat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class ChatNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.d("New FCM Token $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("$message")
    }
}