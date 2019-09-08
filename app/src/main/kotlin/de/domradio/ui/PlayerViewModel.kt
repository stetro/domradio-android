package de.domradio.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import de.domradio.radio.RadioState
import de.domradio.usecase.PlayerVisibleUseCase
import de.domradio.usecase.RadioUseCase
import de.domradio.usecase.StationInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class PlayerViewModel(
    private val stationInfoUseCase: StationInfoUseCase,
    private val radioUseCase: RadioUseCase,
    private val playerVisibleUseCase: PlayerVisibleUseCase
) : ViewModel() {

    private val title: MutableLiveData<String> = MutableLiveData()
    private val subtitle: MutableLiveData<String> = MutableLiveData()
    private val radioState: MutableLiveData<RadioState> = MutableLiveData()
    private val isPlayerVisible: MutableLiveData<Boolean> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable? = null

    fun startRadioConnection() {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
        compositeDisposable?.add(radioUseCase.getRadioState().subscribe {
            radioState.value = it
        })
        compositeDisposable?.add(stationInfoUseCase.pollStationInformation().subscribe {
            subtitle.value = "${it.title} - ${it.artist}"
        })
        compositeDisposable?.add(
            playerVisibleUseCase.isPlayerVisible().subscribe(
                { if (isPlayerVisible.value != it) isPlayerVisible.value = it },
                { Timber.e(it) })
        )
    }

    fun stopRadioConnection() {
        compositeDisposable?.dispose()
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

    fun updateIsPlayerVisible(destination: NavDestination) =
        playerVisibleUseCase.updateNavigation(destination)
}