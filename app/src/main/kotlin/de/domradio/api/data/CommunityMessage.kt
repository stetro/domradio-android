package de.domradio.api.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CommunityMessage(
    var author: String? = null,
    var timestamp: Long? = null,
    var text: String? = null
)
