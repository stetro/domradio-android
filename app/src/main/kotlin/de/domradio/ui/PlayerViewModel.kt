package de.domradio.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import de.domradio.R
import de.domradio.radio.RadioState
import de.domradio.usecase.RadioUseCase
import de.domradio.usecase.StationInfoUseCase
import io.reactivex.disposables.Disposable

class PlayerViewModel(private val stationInfoUseCase: StationInfoUseCase, private val radioUseCase: RadioUseCase) :
    ViewModel() {

    private val title: MutableLiveData<String> = MutableLiveData()
    private val subtitle: MutableLiveData<String> = MutableLiveData()
    private val radioState: MutableLiveData<RadioState> = MutableLiveData()
    private val isPlayerVisible: MutableLiveData<Boolean> = MutableLiveData()
    private var pollStationInformationSubscription: Disposable? = null
    private var stateSubscription: Disposable? = null

    fun startRadioConnection() {
        stateSubscription?.dispose()
        stateSubscription = radioUseCase.getRadioState().subscribe {
            radioState.value = it
        }
        pollStationInformationSubscription?.dispose()
        pollStationInformationSubscription = stationInfoUseCase
            .pollStationInformation().subscribe {
                subtitle.value = "${it.title} - ${it.artist}"
            }
    }

    fun stopRadioConnection() {
        pollStationInformationSubscription?.dispose()
        stateSubscription?.dispose()
    }

    fun getTitle(): LiveData<String> = title

    fun getSubtitle(): LiveData<String> = subtitle

    fun getRadioState(): LiveData<RadioState> = radioState

    fun isPlayerVisible(): LiveData<Boolean> = isPlayerVisible

    fun playButtonPressed(): Boolean {
        return when (radioState.value) {
            RadioState.BUFFERING -> radioUseCase.stopStream()
            RadioState.PLAYING -> radioUseCase.stopStream()
            else -> radioUseCase.startStream()
        }
    }

    fun updateIsPlayerVisible(destination: NavDestination) {
        when (destination.id) {
            R.id.homeFragment -> isPlayerVisible.value = true
            else -> isPlayerVisible.value = false
        }
    }
}