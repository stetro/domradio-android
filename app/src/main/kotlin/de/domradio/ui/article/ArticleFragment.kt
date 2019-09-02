package de.domradio.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.domradio.R
import de.domradio.utils.FragmentExtensions.getNavController
import kotlinx.android.synthetic.main.article_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ArticleFragment : Fragment() {

    private val articleViewModel: ArticleViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.article_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        val article = ArticleFragmentArgs.fromBundle(arguments!!).article
        articleViewModel.open(article)
        observeModel()
        observeView()
    }

    private fun observeView() {
        article_fragment_web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                articleViewModel.setProgress(newProgress)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                getNavController()?.navigate(ArticleFragmentDirections.actionArticleFragmentToSettingsFragment())
                return true
            }
            R.id.menu_share -> {
                shareArticle()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareArticle() {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"

        val article = articleViewModel.getArticle().value
        if (article != null) {
            share.putExtra(Intent.EXTRA_SUBJECT, article.title)
            share.putExtra(Intent.EXTRA_TEXT, article.url)
            startActivity(Intent.createChooser(share, getString(R.string.share_article)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun observeModel() {
        articleViewModel.getArticle().observe(viewLifecycleOwner, Observer { article ->
            (activity as AppCompatActivity).supportActionBar?.title = article.title
            article_fragment_web_view.loadUrl(article.url)
        })

        articleViewModel.getProgress().observe(viewLifecycleOwner, Observer { progress ->
            if (progress >= 100) {
                article_fragment_progress_bar.isVisible = false
            } else {
                article_fragment_progress_bar.isVisible = true
                article_fragment_progress_bar.progress = progress
            }
        })
    }

}