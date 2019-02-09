package de.domradio

import android.app.Application
import de.domradio.api.RetrofitConfiguration
import de.domradio.ui.article.ArticleViewModel
import de.domradio.ui.articlelist.ArticleListViewModel
import de.domradio.usecase.ArticleListUseCase
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import timber.log.Timber

class DomradioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(viewModelModule, apiModule, systemModel))
        Timber.plant(Timber.DebugTree())
    }

}

val viewModelModule = module {
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleViewModel() }
}

val apiModule = module {
    single { RetrofitConfiguration.getHttpClient() }
    single("rssRetrofit") { RetrofitConfiguration.getRssRetrofit(get()) }
    single { RetrofitConfiguration.getRssService(get("rssRetrofit")) }
}

val systemModel = module {
    single { ArticleListUseCase(get()) }
}