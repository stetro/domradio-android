package de.domradio.ui.home

import androidx.lifecycle.ViewModel
import de.domradio.usecase.PlayerVisibleUseCase

class HomeViewModel(private val playerVisibleUseCase: PlayerVisibleUseCase) : ViewModel() {

    fun setViewPagerPosition(position: Int) = playerVisibleUseCase.updateHomeViewPagerPosition(position)
}

