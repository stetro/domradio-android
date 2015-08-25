package de.domradio.activity.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import de.domradio.activity.dialog.ConfirmNoWifiDialog;
import de.domradio.service.RadioService;
import de.domradio.service.event.StartRadioEvent;
import de.domradio.service.event.StopRadioEvent;
import de.greenrobot.event.EventBus;


public class PlayButtonOnClickListener implements View.OnClickListener {
    private Context context;

    public PlayButtonOnClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (RadioService.get_state()) {
            case STARTING:
                EventBus.getDefault().post(new StopRadioEvent());
                break;
            case PLAYING:
                EventBus.getDefault().post(new StopRadioEvent());
                break;
            default:
                startRadio();
                break;
        }
    }

    private void startRadio() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            EventBus.getDefault().post(new StartRadioEvent());
        } else {
            new ConfirmNoWifiDialog(context).show();
        }
    }
}
