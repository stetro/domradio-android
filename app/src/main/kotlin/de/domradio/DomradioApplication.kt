package de.domradio

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import de.domradio.api.RetrofitConfiguration
import de.domradio.radio.RadioService
import de.domradio.ui.PlayerViewModel
import de.domradio.ui.article.ArticleViewModel
import de.domradio.ui.articlelist.ArticleListViewModel
import de.domradio.usecase.ArticleListUseCase
import de.domradio.usecase.RadioUseCase
import de.domradio.usecase.StationInfoUseCase
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import timber.log.Timber

class DomradioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "DOMRADIO.DE Notification",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService<NotificationManager>()
            notificationManager?.createNotificationChannel(notificationChannel)
        }
        startKoin(this, listOf(viewModelModule, apiModule, systemModel))
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID: String = "domradio_notification"
        const val NOTIFICATION_ID = 1000
    }
}

val viewModelModule = module {
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleViewModel() }
    viewModel { PlayerViewModel(get(), get()) }
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
    single { RadioUseCase(androidContext(), get()) }
    single { StationInfoUseCase(get(), androidContext()) }
    single { Intent(androidContext(), RadioService::class.java) }
}