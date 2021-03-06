package de.domradio.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import de.domradio.R
import de.domradio.radio.RadioState
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val playerViewModel: PlayerViewModel by viewModel()
    private val playerConstraintSet = ConstraintSet()
    private val noPlayerConstraintSet = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupActionBarWithNavController(findNavController(R.id.main_navigation_host))
        playerConstraintSet.clone(main_activity_constraint_layout)
        noPlayerConstraintSet.clone(this, R.layout.main_activity_no_player)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_navigation_host).navigateUp()

    override fun onResume() {
        super.onResume()
        observeViews()
        observeModels()
        playerViewModel.startRadioConnection()
        findNavController(R.id.main_navigation_host).addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) = playerViewModel.updateIsPlayerVisible(destination)

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
        playerViewModel.isPlayerVisible().observe(this, Observer { isPlayerVisible ->
            Timber.d("visible of player $isPlayerVisible")
            TransitionManager.beginDelayedTransition(main_activity_constraint_layout)
            if (isPlayerVisible) {
                playerConstraintSet.applyTo(main_activity_constraint_layout)
            } else {
                noPlayerConstraintSet.applyTo(main_activity_constraint_layout)
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
        findNavController(R.id.main_navigation_host).removeOnDestinationChangedListener(this)
    }
}
