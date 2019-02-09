package de.domradio.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.domradio.usecase.StationInfoUseCase
import io.reactivex.disposables.Disposable

class PlayerViewModel(private val stationInfoUseCase: StationInfoUseCase) : ViewModel() {

    private val title: MutableLiveData<String> = MutableLiveData()
    private val subtitle: MutableLiveData<String> = MutableLiveData()
    private val playerState: MutableLiveData<PlayerState> = MutableLiveData()
    private var pollStationInformationSubscription: Disposable? = null;

    fun startStationInformation() {
        pollStationInformationSubscription?.dispose()
        pollStationInformationSubscription = stationInfoUseCase.pollStationInformation()
            .subscribe {
                subtitle.value = "${it.title} - ${it.artist}"
            }
    }

    fun stopStationInformation() {
        pollStationInformationSubscription?.dispose()
    }

    fun getTitle(): LiveData<String> = title

    fun getSubtitle(): LiveData<String> = subtitle

    fun getPlayerState(): LiveData<PlayerState> = playerState

    fun playButtonPressed() {

    }

    enum class PlayerState {
        STOPPED, BUFFERING, PLAYING
    }

}