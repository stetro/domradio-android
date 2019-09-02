package de.domradio.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import de.domradio.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}