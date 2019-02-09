package de.domradio.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.domradio.R
import kotlinx.android.synthetic.main.article_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleFragment : Fragment() {

    private val articleViewModel: ArticleViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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