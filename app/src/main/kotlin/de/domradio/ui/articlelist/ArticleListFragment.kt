package de.domradio.ui.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import de.domradio.R
import de.domradio.utils.FragmentExtensions.getNavController
import kotlinx.android.synthetic.main.article_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleListFragment : Fragment() {

    private val articleListViewModel: ArticleListViewModel by viewModel()
    private val articleListAdapter = ArticleListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        articleListViewModel.reset()
        articleListViewModel.refresh()
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.article_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        articleListAdapter.clickListener = View.OnClickListener { view ->
            val position = article_list_fragment_recycler_view.getChildLayoutPosition(view)
            val article = articleListAdapter.articles[position]
            getNavController()?.navigate(
                ArticleListFragmentDirections.actionArticleListFragmentToArticleFragment(article)
            )
        }

        article_list_fragment_recycler_view.adapter = articleListAdapter
        article_list_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            getNavController()?.navigate(ArticleListFragmentDirections.actionArticleListFragmentToSettingsFragment())
            return true
        }
        return super.onOptionsItemSelected(item)
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