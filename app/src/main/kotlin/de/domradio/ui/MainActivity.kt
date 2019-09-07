package de.domradio.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import de.domradio.R
import de.domradio.radio.RadioState
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
        playerViewModel.startRadioConnection()
    }

    private fun observeModels() {
        playerViewModel.getTitle().observe(this, Observer { title ->
            main_activity_player_title.text = title
        })
        playerViewModel.getSubtitle().observe(this, Observer { subtitle ->
            main_activity_player_subtitle.text = subtitle
        })
        playerViewModel.getRadioState().observe(this, Observer { state ->
            when (state) {
                RadioState.BUFFERING ->
                    main_activity_player_button.setImageResource(R.drawable.ic_wifi_load_white_24dp)
                RadioState.PLAYING ->
                    main_activity_player_button.setImageResource(R.drawable.ic_stop_white_24dp)
                else ->
                    main_activity_player_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }
        })
    }

    private fun observeViews() {
        main_activity_player_button.setOnClickListener {
            if (!playerViewModel.playButtonPressed()) {
                MaterialDialog(this).show {
                    title(R.string.cellular_streaming_disabled)
                    message(R.string.cellular_streaming_disabled_text)
                    positiveButton(R.string.ok)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_rate -> {
                showRateAppDialog()
                true
            }
            R.id.menu_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        MaterialDialog(this).show {
            title(R.string.about)
            message(R.string.about_text) {
                html {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse("https://domradio.de")
                    startActivity(i)
                }
            }
            positiveButton(R.string.ok)
        }
    }

    private fun showRateAppDialog() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.stopRadioConnection()
    }
}
