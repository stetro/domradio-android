package de.domradio.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.domradio.usecase.data.Article

class ArticleViewModel : ViewModel() {

    private val article: MutableLiveData<Article> = MutableLiveData()

    private val progress: MutableLiveData<Int> = MutableLiveData()

    fun open(article: Article) {
        this.article.value = article
    }

    fun setProgress(progress: Int) {
        this.progress.value = progress
    }

    fun getArticle(): LiveData<Article> = article

    fun getProgress(): LiveData<Int> = progress

}