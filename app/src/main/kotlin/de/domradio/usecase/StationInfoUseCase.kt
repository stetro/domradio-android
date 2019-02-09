package de.domradio.usecase

import android.content.Context
import de.domradio.R
import de.domradio.api.StationService
import de.domradio.api.data.StationInformation
import de.domradio.usecase.data.StationInfo
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit


class StationInfoUseCase(private val stationService: StationService, private val context: Context) {

    fun pollStationInformation(): Observable<StationInfo> {
        return Observable
            .interval(0, 10, TimeUnit.SECONDS)
            .flatMap<StationInfo> { queryStationInformation() }
    }

    private fun queryStationInformation(): Observable<StationInfo> {
        val subject: PublishSubject<StationInfo> = PublishSubject.create()
        stationService.getStationInformation().enqueue(object : Callback<StationInformation> {
            override fun onFailure(call: Call<StationInformation>, t: Throwable) {
                Timber.w(t, "onFailure: ")
                subject.onError(t)
                subject.onComplete()
            }

            override fun onResponse(
                call: Call<StationInformation>,
                response: Response<StationInformation>
            ) {
                response.body()?.let {
                    subject.onNext(StationInfo(it.onair?.title, it.onair?.artist))
                    subject.onComplete()
                } ?: run {
                    subject.onError(Exception("Empty payload"))
                    subject.onComplete()
                }
            }
        })
        return subject.onErrorResumeNext(
            Observable.just(
                StationInfo(
                    context.getString(R.string.app_name),
                    context.getString(R.string.app_slogan)
                )
            )
        )
    }

}