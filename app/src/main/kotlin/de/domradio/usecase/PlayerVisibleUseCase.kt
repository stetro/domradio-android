package de.domradio.usecase

import androidx.navigation.NavDestination
import de.domradio.R
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class PlayerVisibleUseCase {
    private val isPlayerVisible = BehaviorSubject.create<Boolean>()

    fun isPlayerVisible(): Observable<Boolean> = isPlayerVisible

    private var destination: NavDestination? = null
    private var viewPagerPosition = 0

    fun updateNavigation(destination: NavDestination) {
        Timber.d("destination ${destination.label}")
        this.destination = destination
        updatePlayerVisibility()
    }

    fun updateHomeViewPagerPosition(position: Int) {
        Timber.d("viewPager position $position")
        this.viewPagerPosition = position
        updatePlayerVisibility()
    }

    private fun updatePlayerVisibility() {
        when (destination?.id) {
            R.id.homeFragment -> isPlayerVisible.onNext(viewPagerPosition == 0)
            else -> isPlayerVisible.onNext(true)
        }
    }
}
