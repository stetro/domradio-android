package de.domradio.usecase

import de.domradio.api.RssService
import de.domradio.ui.articlelist.ArticleListViewModel
import de.domradio.usecase.data.Article
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.ParseException
import java.util.*

class ArticleListUseCase(private val rssService: RssService) {

    companion object {
        const val BASIC_FEED_URL = "/rss-feeds/domradio-rss.xml"
    }

    fun getFeed(): Observable<List<Article>> {

        val subject = PublishSubject.create<List<Article>>()

        rssService.getRss(BASIC_FEED_URL).enqueue(object : Callback<RssFeed> {
            override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                subject.onError(t)
            }

            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                response.body()?.items?.let { items ->
                    subject.onNext(mapRssItemsToArticles(items))
                } ?: run {
                    subject.onError(Exception("Empty Rss Feed"))
                }
            }
        })
        return subject
    }

    private fun mapRssItemsToArticles(items: List<RssItem>): List<Article> {
        return items.filter {
            it.title != null && it.description != null && it.publishDate != null
        }.map { item ->
            val description = item.description!!.replace(Regex("<.*?>"), "")
            var date = item.publishDate!!
            try {
                date = ArticleListViewModel.sourceDateFormat.parse(date)?.let { javaDate ->
                    ArticleListViewModel.destinationDateFormat.format(javaDate)
                } ?: run {
                    ArticleListViewModel.destinationDateFormat.format(Date())
                }
            } catch (e: ParseException) {
                Timber.w(e, "onResponse: could not transform date")
            }
            return@map Article(
                item.title!!.trim(),
                date.trim(),
                description.trim(),
                item.link!!.trim()
            )
        }
    }
}