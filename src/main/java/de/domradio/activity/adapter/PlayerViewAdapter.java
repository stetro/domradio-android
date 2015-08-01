package de.domradio.activity.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;

import de.domradio.R;
import de.domradio.activity.util.PlayButtonOnClickListener;
import de.domradio.service.RadioService;
import de.domradio.service.RadioServiceState;
import de.domradio.service.event.RadioStartedEvent;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.RadioStoppedEvent;
import de.greenrobot.event.EventBus;


public class PlayerViewAdapter implements ViewAdapter {

    private FloatingActionButton playerButton;
    private TextView playerInfoText;
    private FABProgressCircle playerProgressCircle;

    @Override
    public void register(Activity activity) {
        bindViews(activity);
        updatePlayerState();
        startRadioService(activity);
        EventBus.getDefault().register(this);
    }

    private void bindViews(Activity activity) {
        playerButton = (FloatingActionButton) activity.findViewById(R.id.radio_fragment_button);
        playerButton.setOnClickListener(new PlayButtonOnClickListener(activity));
        playerInfoText = (TextView) activity.findViewById(R.id.radio_fragment_text);
        playerProgressCircle = (FABProgressCircle) activity.findViewById(R.id.radio_fragment_button_circle);
    }

    @Override
    public void unregister(Activity activity) {
        if (RadioService.get_state().equals(RadioServiceState.STOPPED)) {
            activity.stopService(new Intent(activity, RadioService.class));
        }
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
                    playerButton.setImageResource(R.drawable.ic_play);
                    playerInfoText.setText(R.string.radio_live_stream_loading);
                    if (playerProgressCircle != null && !playerProgressCircle.isActivated()) {
                        playerProgressCircle.show();
                    }
                    break;
                case PLAYING:
                    playerButton.setImageResource(R.drawable.ic_pause);
                    playerInfoText.setText(R.string.radio_live_stream);
                    if (playerProgressCircle != null && playerProgressCircle.isShown()) {
                        playerProgressCircle.hide();
                    }
                    break;
                case STOPPED:
                    playerButton.setImageResource(R.drawable.ic_play);
                    playerInfoText.setText(R.string.radio_live_stream);
                    if (playerProgressCircle != null && playerProgressCircle.isShown()) {
                        playerProgressCircle.hide();
                    }
                    break;
            }
        }
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

}
