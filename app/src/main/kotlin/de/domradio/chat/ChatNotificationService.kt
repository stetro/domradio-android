package de.domradio.chat

import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class ChatNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.d("New FCM Token $token")
    }
}