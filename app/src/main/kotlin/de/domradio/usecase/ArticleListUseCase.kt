package de.domradio.usecase

import de.domradio.api.RssService
import me.toptas.rssconverter.RssFeed
import org.koin.standalone.KoinComponent
import retrofit2.Call

class ArticleListUseCase(private val rssService: RssService) {

    fun getFeed(): Call<RssFeed> = rssService.getRss("/rss-feeds/domradio-rss.xml")
}
