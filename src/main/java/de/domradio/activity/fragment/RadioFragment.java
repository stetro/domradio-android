package de.domradio.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.domradio.R;
import de.domradio.activity.dialog.ConfirmNoWifiDialog;
import de.domradio.service.RadioService;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.StartRadioEvent;
import de.domradio.service.event.StopRadioEvent;
import de.greenrobot.event.EventBus;


public class RadioFragment extends Fragment implements View.OnClickListener {

    private ImageButton button;
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
                EventBus.getDefault().post(new StopRadioEvent());
                break;
            default:
                startRadio();
                break;
        }
    }

    private void startRadio() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            EventBus.getDefault().post(new StartRadioEvent());
        } else {
            new ConfirmNoWifiDialog(getActivity()).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updatePlayerState() {
        if (button != null && text != null) {
            switch (RadioService.get_state()) {
                case STARTING:
                    button.setImageResource(R.drawable.ic_play);
                    text.setText(R.string.radio_live_stream_loading);
                    break;
                case PLAYING:
                    button.setImageResource(R.drawable.ic_pause);
                    text.setText(R.string.radio_live_stream);
                    break;
                case STOPPED:
                    button.setImageResource(R.drawable.ic_play);
                    text.setText(R.string.radio_live_stream);
                    break;
            }
        }
    }
}
