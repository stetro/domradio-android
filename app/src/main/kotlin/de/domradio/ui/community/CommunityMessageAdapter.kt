package de.domradio.ui.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.domradio.R
import de.domradio.api.data.CommunityMessage
import java.util.*


open class CommunityMessageAdapter(firebaseRecyclerOptions: FirebaseRecyclerOptions<CommunityMessage>) :
    FirebaseRecyclerAdapter<CommunityMessage, CommunityMessageAdapter.CommunityMessageHolder>(
        firebaseRecyclerOptions
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityMessageHolder {
        return CommunityMessageHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.community_message_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        communityMessageHolder: CommunityMessageHolder,
        position: Int,
        communityMessage: CommunityMessage
    ) {
        communityMessageHolder.bindData(communityMessage)
    }


    class CommunityMessageHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val author = view.findViewById<TextView>(R.id.community_message_item_author)
        private val timestamp = view.findViewById<TextView>(R.id.community_message_item_timestamp)
        private val text = view.findViewById<TextView>(R.id.community_message_item_text)

        fun bindData(communityMessage: CommunityMessage) {
            this.author.text = communityMessage.author
            this.text.text = communityMessage.text
            communityMessage.timestamp?.let {
                this.timestamp.text = CommunityViewModel.messageDateFormat.format(Date(it * 1000))
            }
        }
    }
}
