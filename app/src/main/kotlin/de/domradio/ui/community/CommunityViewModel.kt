package de.domradio.ui.community

import androidx.lifecycle.ViewModel
import de.domradio.api.CommunityMessageRepository
import java.text.SimpleDateFormat
import java.util.*

class CommunityViewModel(private val communityMessageRepository: CommunityMessageRepository) :
    ViewModel() {

    fun getCommunityMessagesQuery() = communityMessageRepository.getCommunityMessagesQuery()

    companion object {
        var messageDateFormat = SimpleDateFormat("dd. MMM yyyy HH:mm", Locale.GERMAN)
    }
}
