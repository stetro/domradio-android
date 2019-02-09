package de.domradio

import android.app.Application
import de.domradio.api.RetrofitConfiguration
import de.domradio.ui.PlayerViewModel
import de.domradio.ui.article.ArticleViewModel
import de.domradio.ui.articlelist.ArticleListViewModel
import de.domradio.usecase.ArticleListUseCase
import de.domradio.usecase.StationInfoUseCase
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
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
    viewModel { PlayerViewModel(get()) }
}

val apiModule = module {
    single { RetrofitConfiguration.getHttpClient() }
    single("rssRetrofit") { RetrofitConfiguration.getRssRetrofit(get()) }
    single { RetrofitConfiguration.getRssService(get("rssRetrofit")) }
    single("playerRetrofit") { RetrofitConfiguration.getPlayerRetrofit(get()) }
    single { RetrofitConfiguration.getStationService(get("playerRetrofit")) }
}

val systemModel = module {
    single { ArticleListUseCase(get()) }
    single { StationInfoUseCase(get(), androidContext()) }
}