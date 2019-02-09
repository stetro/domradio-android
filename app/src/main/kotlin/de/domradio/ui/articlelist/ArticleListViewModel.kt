package de.domradio.ui.articlelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.domradio.usecase.ArticleListUseCase
import de.domradio.usecase.data.Article
import java.text.SimpleDateFormat
import java.util.*


class ArticleListViewModel(private val articleListUseCase: ArticleListUseCase) : ViewModel() {

    private val refreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val articles: MutableLiveData<List<Article>> = MutableLiveData()


    fun reset() {
        refreshing.value = false
    }

    fun refresh() {
        refreshing.value = true
        val subscribtion = articleListUseCase.getFeed()
            .subscribe({
                refreshing.value = false
                articles.value = it
            }, {
                // TODO Error
                refreshing.value = false
            })
    }


    fun getRefreshing(): LiveData<Boolean> = refreshing

    fun getArticles(): LiveData<List<Article>> = articles

    companion object {
        var sourceDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
        var destinationDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }

}