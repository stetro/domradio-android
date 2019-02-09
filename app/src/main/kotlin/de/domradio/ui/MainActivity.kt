package de.domradio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import de.domradio.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupActionBarWithNavController(findNavController(R.id.main_navigation_host))
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.main_navigation_host).navigateUp()
}
