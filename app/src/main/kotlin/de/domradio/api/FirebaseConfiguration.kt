package de.domradio.api

import com.google.firebase.database.FirebaseDatabase

object FirebaseConfiguration {

    fun getDatabase(): FirebaseDatabase {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
        return firebaseDatabase
    }
}
