package de.domradio.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;

import de.domradio.R;
import de.domradio.activity.dialog.AboutDialog;
import de.domradio.activity.util.AppRating;
import de.domradio.activity.util.PlayButtonOnClickListener;
import de.domradio.service.RadioService;
import de.domradio.service.RadioServiceState;
import de.domradio.service.event.ErrorEvent;
import de.domradio.service.event.RadioStartedEvent;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.RadioStoppedEvent;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements FABProgressListener {

    public volatile static boolean isRunning = false;
    private FloatingActionButton playerButton;
    private TextView playerInfoText;
    private FABProgressCircle progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);
        playerButton = (FloatingActionButton) findViewById(R.id.radio_fragment_button);
        playerButton.setOnClickListener(new PlayButtonOnClickListener(this));
        playerInfoText = (TextView) findViewById(R.id.radio_fragment_text);
        progressCircle = (FABProgressCircle) findViewById(R.id.radio_fragment_button_circle);
        progressCircle.attachListener(this);
        updatePlayerState();
        startRadioService();
        isRunning = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_acticity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_menu_about:
                new AboutDialog(this).show();
                return true;
            case R.id.main_activity_menu_rate:
                AppRating.rateThisApp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRadioService() {
        Intent intent = new Intent(getApplicationContext(), RadioService.class);
        this.startService(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (RadioService.get_state().equals(RadioServiceState.STOPPED)) {
            stopService(new Intent(this, RadioService.class));
        }
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RadioStartingEvent e) {
        updatePlayerState();
    }

    public void onEvent(RadioStartedEvent e) {
        updatePlayerState();
    }

    public void onEvent(RadioStoppedEvent e) {
        updatePlayerState();
    }

    public void onEvent(ErrorEvent e) {
        Snackbar.make(findViewById(R.id.root_view), e.getMessage(), Snackbar.LENGTH_LONG).show();
    }


    private void updatePlayerState() {
        if (playerButton != null && playerInfoText != null) {
            switch (RadioService.get_state()) {
                case STARTING:
                    playerButton.setImageResource(R.drawable.ic_play);
                    playerInfoText.setText(R.string.radio_live_stream_loading);
                    if (progressCircle != null && !progressCircle.isActivated()) {
                        progressCircle.show();
                    }
                    break;
                case PLAYING:
                    playerButton.setImageResource(R.drawable.ic_pause);
                    playerInfoText.setText(R.string.radio_live_stream);
                    if (progressCircle != null && progressCircle.isShown()) {
                        progressCircle.hide();
                    }
                    break;
                case STOPPED:
                    playerButton.setImageResource(R.drawable.ic_play);
                    playerInfoText.setText(R.string.radio_live_stream);
                    if (progressCircle != null && progressCircle.isShown()) {
                        progressCircle.hide();
                    }
                    break;
            }
        }
    }

    @Override
    public void onFABProgressAnimationEnd() {

    }
}
