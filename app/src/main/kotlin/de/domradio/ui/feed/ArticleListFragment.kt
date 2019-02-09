package de.domradio.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import de.domradio.R
import kotlinx.android.synthetic.main.article_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment : Fragment() {

    private val articleListViewModel: ArticleListViewModel by viewModel()
    private val articleListAdapter = ArticleListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.article_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleListViewModel.reset()
        articleListViewModel.refresh()

        article_list_fragment_recycler_view.adapter = articleListAdapter
        article_list_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        observeModels()
        observeViews()
    }

    private fun observeViews() {
        article_list_fragment_swipe_refresh_layout.setOnRefreshListener { articleListViewModel.refresh() }
    }

    private fun observeModels() {
        articleListViewModel.getRefreshing().observe(viewLifecycleOwner, Observer { refreshing ->
            article_list_fragment_swipe_refresh_layout.isRefreshing = refreshing
        })
        articleListViewModel.getArticles().observe(viewLifecycleOwner, Observer { articles ->
            articleListAdapter.articles.clear()
            articleListAdapter.articles.addAll(articles)
            articleListAdapter.notifyDataSetChanged()
        })
    }

}