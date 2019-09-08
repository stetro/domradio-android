package de.domradio.ui.community

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import de.domradio.R
import de.domradio.api.data.CommunityMessage
import kotlinx.android.synthetic.main.community_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityFragment : Fragment(R.layout.community_fragment) {

    private lateinit var adapter: CommunityMessageAdapter
    private val communityViewModel: CommunityViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = FirebaseRecyclerOptions.Builder<CommunityMessage>()
            .setQuery(communityViewModel.getCommunityMessagesQuery(), CommunityMessage::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = object : CommunityMessageAdapter(options) {
            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(context, "Error loading data ${error.message}", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onDataChanged() {
                super.onDataChanged()
                community_fragment_recycler_view.scrollToPosition(adapter.itemCount-1)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        community_fragment_recycler_view.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = false
        community_fragment_recycler_view.layoutManager = linearLayoutManager
    }
}
