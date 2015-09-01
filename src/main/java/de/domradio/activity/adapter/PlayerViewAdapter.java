package de.domradio.activity.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import de.domradio.R;
import de.domradio.activity.MainActivity;
import de.domradio.activity.util.PlayButtonOnClickListener;
import de.domradio.service.EventBusCallback;
import de.domradio.service.RadioService;
import de.domradio.service.RadioServiceState;
import de.domradio.service.TitleRefreshRunnable;
import de.domradio.service.event.RadioStartedEvent;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.RadioStoppedEvent;
import de.domradio.service.event.UpdatePlayerTitleEvent;
import de.greenrobot.event.EventBus;


public class PlayerViewAdapter implements ViewAdapter {

    private FloatingActionButton playerButton;
    private TextView playerInfoText;
    private TextView playerTitleText;
    private TitleRefreshRunnable titleRefreshThread;
    private MainActivity activity;

    @Override
    public void register(MainActivity activity) {
        this.activity = activity;
        bindViews(activity);
        updatePlayerState();
        startRadioService(activity);
        EventBus.getDefault().register(this);
        titleRefreshThread = new TitleRefreshRunnable();
        new Thread(titleRefreshThread).start();
    }

    private void bindViews(MainActivity activity) {
        playerButton = (FloatingActionButton) activity.findViewById(R.id.radio_fragment_button);
        playerButton.setOnClickListener(new PlayButtonOnClickListener(activity));
        playerInfoText = (TextView) activity.findViewById(R.id.radio_fragment_text);
        playerTitleText = (TextView) activity.findViewById(R.id.radio_fragment_title);
        //playerTitleText.setHorizontallyScrolling(true);
    }

    @Override
    public void unregister(MainActivity activity) {
        if (RadioService.get_state().equals(RadioServiceState.STOPPED)) {
            activity.stopService(new Intent(activity, RadioService.class));
        }
        titleRefreshThread.setRunning(false);
        EventBus.getDefault().unregister(this);
    }

    private void startRadioService(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), RadioService.class);
        activity.startService(intent);
    }


    private void updatePlayerState() {
        if (playerButton != null && playerInfoText != null) {
            switch (RadioService.get_state()) {
                case STARTING:
                    playerButton.setImageResource(R.drawable.ic_pause);
                    playerInfoText.setText(R.string.radio_live_stream_loading);
                    break;
                case PLAYING:
                    playerButton.setImageResource(R.drawable.ic_pause);
                    playerInfoText.setText(R.string.radio_live_stream);
                    break;
                case STOPPED:
                    playerButton.setImageResource(R.drawable.ic_play);
                    playerInfoText.setText(R.string.radio_live_stream);
                    break;
            }
        }
    }

    @EventBusCallback
    public void onEvent(RadioStartingEvent e) {
        updatePlayerState();
    }

    @EventBusCallback
    public void onEvent(RadioStartedEvent e) {
        updatePlayerState();
    }

    @EventBusCallback
    public void onEvent(RadioStoppedEvent e) {
        updatePlayerState();
    }

    @EventBusCallback
    public void onEvent(final UpdatePlayerTitleEvent e) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e.getStation().onair.type.equals("I")) {
                    playerTitleText.setVisibility(View.GONE);
                } else {
                    String artist = e.getStation().onair.artist.replaceAll("([^,]*),([^,]*)", "$2 $1");
                    String title = artist + " - " + e.getStation().onair.title;
                    if (!playerTitleText.getText().equals(title)) {
                        playerTitleText.setVisibility(View.GONE);
                        playerTitleText.setText(title);
                        playerTitleText.setVisibility(View.VISIBLE);
                        playerTitleText.setSelected(true);
                    }
                }
            }
        });
    }
}
