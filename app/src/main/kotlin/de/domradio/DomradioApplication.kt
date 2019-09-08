package de.domradio

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import de.domradio.api.CommunityMessageRepository
import de.domradio.api.FirebaseConfiguration
import de.domradio.api.RetrofitConfiguration
import de.domradio.radio.RadioService
import de.domradio.ui.PlayerViewModel
import de.domradio.ui.article.ArticleViewModel
import de.domradio.ui.articlelist.ArticleListViewModel
import de.domradio.ui.community.CommunityViewModel
import de.domradio.ui.home.HomeViewModel
import de.domradio.usecase.ArticleListUseCase
import de.domradio.usecase.PlayerVisibleUseCase
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
        // setup Koin
        startKoin(this, listOf(viewModelModule, apiModule, systemModel))
        // setup Timber
        Timber.plant(Timber.DebugTree())
        // setup Firebase
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnSuccessListener { Timber.d("Signed in to Firebase") }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            Timber.d("FCM Token ${task.result?.token}")
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID: String = "domradio_notification"
        const val NOTIFICATION_ID = 1000
    }
}

val viewModelModule = module {
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleViewModel() }
    viewModel { PlayerViewModel(get(), get(), get()) }
    viewModel { CommunityViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val apiModule = module {
    single { RetrofitConfiguration.getHttpClient() }
    single("rssRetrofit") { RetrofitConfiguration.getRssRetrofit(get()) }
    single { RetrofitConfiguration.getRssService(get("rssRetrofit")) }
    single("playerRetrofit") { RetrofitConfiguration.getPlayerRetrofit(get()) }
    single { RetrofitConfiguration.getStationService(get("playerRetrofit")) }
    single { CommunityMessageRepository(get()) }
    single { FirebaseConfiguration.getDatabase() }
    single { PlayerVisibleUseCase() }
}

val systemModel = module {
    single { ArticleListUseCase(get()) }
    single { RadioUseCase(androidContext(), get(), get()) }
    single { StationInfoUseCase(get(), androidContext()) }
    single { Intent(androidContext(), RadioService::class.java) }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}