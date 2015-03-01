package de.stetro.domradio.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;

import de.greenrobot.event.EventBus;
import de.stetro.domradio.R;
import de.stetro.domradio.service.RadioService;
import de.stetro.domradio.service.event.RadioStartingEvent;
import de.stetro.domradio.service.event.StartRadioEvent;
import de.stetro.domradio.service.event.StopRadioEvent;


public class RadioFragment extends Fragment implements View.OnClickListener {

    private ImageButton button;
    private ProgressBarIndeterminateDeterminate progress;
    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radio_fragment, container, false);
        button = (ImageButton) view.findViewById(R.id.radio_fragment_button);
        button.setOnClickListener(this);
        progress = (ProgressBarIndeterminateDeterminate) view.findViewById(R.id.radio_fragment_progress);
        text = (TextView) view.findViewById(R.id.radio_fragment_text);
        updatePlayerState();
        return view;
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

    @Override
    public void onClick(View v) {
        switch (RadioService.get_state()) {
            case PLAYING:
                EventBus.getDefault().post(new StopRadioEvent(false));
                break;
            default:
                EventBus.getDefault().post(new StartRadioEvent(false));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updatePlayerState() {
        if (button != null && progress != null && text != null) {
            switch (RadioService.get_state()) {
                case STARTING:
                    progress.setVisibility(View.VISIBLE);
                    button.setImageResource(R.drawable.ic_play);
                    text.setText(R.string.radio_live_stream_loading);
                    break;
                case PLAYING:
                    progress.setVisibility(View.VISIBLE);
                    button.setImageResource(R.drawable.ic_pause);
                    text.setText(R.string.radio_live_stream);
                    break;
                case STOPPED:
                    progress.setVisibility(View.INVISIBLE);
                    button.setImageResource(R.drawable.ic_play);
                    text.setText(R.string.radio_live_stream);
                    break;
            }
        }
    }
}
