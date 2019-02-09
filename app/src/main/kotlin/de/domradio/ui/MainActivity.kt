package de.domradio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import de.domradio.R
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupActionBarWithNavController(findNavController(R.id.main_navigation_host))
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_navigation_host).navigateUp()


    override fun onResume() {
        super.onResume()
        observeViews()
        observeModels()
        playerViewModel.startStationInformation()
    }

    private fun observeModels() {
        playerViewModel.getTitle().observe(this, Observer { title ->
            main_activity_player_title.text = title
        })
        playerViewModel.getSubtitle().observe(this, Observer { subtitle ->
            main_activity_player_subtitle.text = subtitle
        })
        playerViewModel.getPlayerState().observe(this, Observer { state ->
            when (state) {
                PlayerViewModel.PlayerState.BUFFERING ->
                    main_activity_player_button.setImageResource(R.drawable.ic_wifi_load_white_24dp)
                PlayerViewModel.PlayerState.PLAYING ->
                    main_activity_player_button.setImageResource(R.drawable.ic_stop_white_24dp)
                else ->
                    main_activity_player_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }
        })
    }

    private fun observeViews() {
        main_activity_player_button.setOnClickListener {
            playerViewModel.playButtonPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.stopStationInformation()
    }

}
