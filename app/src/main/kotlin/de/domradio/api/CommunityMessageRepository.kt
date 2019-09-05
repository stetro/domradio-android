package de.domradio.api

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class CommunityMessageRepository(database: FirebaseDatabase) {
    private val messages  by lazy{
        database.reference.child("messages")
    }

    fun getCommunityMessagesQuery(): Query = messages
        .limitToLast(10)
        .orderByChild("timestamp")
}