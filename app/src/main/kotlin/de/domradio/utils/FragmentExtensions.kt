package de.domradio.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

object FragmentExtensions {

    fun Fragment.getNavController(): NavController? {
        view?.let {
            return Navigation.findNavController(it)
        }
        return null
    }
}