package de.stetro.domradio.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;

import de.greenrobot.event.EventBus;
import de.stetro.domradio.R;
import de.stetro.domradio.service.StopSuccessRadioEvent;
import de.stetro.domradio.service.event.StartRadioEvent;
import de.stetro.domradio.service.event.StartSuccessRadioEvent;


public class RadioFragment extends Fragment implements View.OnClickListener {

    public static final String RADIO_URL = "http://domradio-mp3-l.akacast.akamaistream.net/7/809/237368/v1/gnl.akacast.akamaistream.net/domradio-mp3-l";
    private ImageButton button;
    private ProgressBarIndeterminateDeterminate progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radio_fragment, container, false);
        button = (ImageButton) view.findViewById(R.id.radio_fragment_button);
        button.setOnClickListener(this);
        progress = (ProgressBarIndeterminateDeterminate) view.findViewById(R.id.radio_fragment_progress);
        return view;
    }

    public void onEvent(StartSuccessRadioEvent e) {
        if (button != null) {
            button.setImageResource(R.drawable.ic_pause);
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(StopSuccessRadioEvent e) {
        if (button != null) {
            button.setImageResource(R.drawable.ic_play);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new StartRadioEvent(RADIO_URL));
    }
}
