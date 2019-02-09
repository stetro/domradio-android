package de.domradio.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.domradio.usecase.ArticleListUseCase
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArticleListViewModel(private val articleListUseCase: ArticleListUseCase) : ViewModel() {

    private val refreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val articles: MutableLiveData<List<RssItem>> = MutableLiveData()


    fun reset() {
        refreshing.value = false
    }

    fun refresh() {
        refreshing.value = true
        articleListUseCase.getFeed()
            .enqueue(object : Callback<RssFeed> {
                override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                    refreshing.value = false
                    // TODO error
                }

                override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                    refreshing.value = false
                    response.body()?.items?.let { items ->
                        articles.value = items
                    } ?: run {
                        // TODO error
                    }

                }
            })
    }

    fun getRefreshing(): LiveData<Boolean> = refreshing
    fun getArticles(): LiveData<List<RssItem>> = articles

}