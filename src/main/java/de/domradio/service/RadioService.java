package de.domradio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.domradio.R;
import de.domradio.activity.MainActivity;
import de.domradio.fragment.RadioStartedEvent;
import de.domradio.fragment.RadioStoppedEvent;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.StartRadioEvent;
import de.domradio.service.event.StopRadioEvent;
import de.greenrobot.event.EventBus;

public class RadioService extends Service implements OnCompletionListener, OnPreparedListener, OnErrorListener {

    public URL m_url = null;
    public MediaPlayer mediaPlayer = null;
    public String errorMessage = "";
    public volatile static RadioServiceState radioServiceState = RadioServiceState.STOPPED;
    private WifiManager.WifiLock wifiLock;

    public static final String RADIO_URL_LOW = "http://domradio-mp3-l.akacast.akamaistream.net/7/809/237368/v1/gnl.akacast.akamaistream.net/domradio-mp3-l";

    public static RadioServiceState get_state() {
        return radioServiceState;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        radioServiceState = RadioServiceState.STOPPED;
        EventBus.getDefault().post(new RadioStoppedEvent());

        Log.d("RadioService", "Track is completed.");
        mp.release();
        if (mp.equals(mediaPlayer)) {
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("RadioService", "MediaPlayer prepared, starting content.");
        mp.start();
        radioServiceState = RadioServiceState.PLAYING;
        EventBus.getDefault().post(new RadioStartedEvent());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("RadioService", "MediaPlayer error.");
        EventBus.getDefault().post(new RadioStoppedEvent());
        Toast.makeText(this, R.string.error_mediaplayer, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.d("RadioService", "Service destroyed.");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        releaseWifiLock();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public RadioService() {
        radioServiceState = RadioServiceState.STOPPED;
        EventBus.getDefault().post(new RadioStoppedEvent());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void acquireWifiLock() {
        WifiManager systemService = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiLock == null) {
            wifiLock = systemService.createWifiLock(WifiManager.WIFI_MODE_FULL, "domradioLock");
        }
        if (!wifiLock.isHeld()) {
            wifiLock.acquire();
        }
    }

    private void releaseWifiLock() {
        if (wifiLock != null && wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    public void onEvent(StartRadioEvent event) {
        errorMessage = "";
        try {
            m_url = new URL(RADIO_URL_LOW);
        } catch (MalformedURLException e) {
            errorMessage += "Error parsing URL (" + RADIO_URL_LOW + "): " + e.toString() + "\n";
            Log.d("RadioService", errorMessage);
        }
        if (errorMessage.equals("")) {
            Log.d("RadioService", "Starting radio ...");
            radioServiceState = RadioServiceState.STARTING;
            EventBus.getDefault().post(new RadioStartingEvent());
            try {
                if (mediaPlayer == null) {
                    Log.d("RadioService", "Looks good, starting station ...");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    mediaPlayer.setDataSource(RADIO_URL_LOW);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
                    mediaPlayer.prepareAsync();
                } else if (!mediaPlayer.isPlaying()) {
                    Log.d("RadioService", "Started but not playing ...");
                    if (radioServiceState != RadioServiceState.STOPPED) {
                        Log.d("RadioService", "Killing current connection...");
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        onEvent(event);
                    } else {
                        Log.d("RadioService", "Restart player");
                        mediaPlayer.start();
                        if (mediaPlayer.isPlaying()) {
                            radioServiceState = RadioServiceState.PLAYING;
                            EventBus.getDefault().post(new RadioStartedEvent());
                        } else {
                            errorMessage += "Error starting stream: object created but wont restart\n";
                        }
                    }
                }
            } catch (IOException e) {
                errorMessage += "Error starting stream: " + e.toString() + "\n";
                Log.e("start", errorMessage, e);
            }
        }
    }

    public void onEvent(StopRadioEvent event) {
        if (mediaPlayer != null) {
            Log.d("RadioService", "MediaPlayer stop, cleaning up.");
            mediaPlayer.reset();
            Log.d("RadioService", "MediaPlayer stop, cleaned up.");
            mediaPlayer.release();
            Log.d("RadioService", "MediaPlayer stop, shut down.");
            mediaPlayer = null;
        }
        radioServiceState = RadioServiceState.STOPPED;
        EventBus.getDefault().post(new RadioStoppedEvent());
    }

    public void onEvent(RadioStartingEvent e) {
        acquireWifiLock();
    }

    public void onEvent(RadioStartedEvent e) {
        acquireWifiLock();
        startForeground(RadioNotification.DEFAULT_NOTIFICATION_ID, RadioNotification.getStickyNotification(this));
    }

    public void onEvent(RadioStoppedEvent e) {
        releaseWifiLock();
        stopForeground(true);
        if (!MainActivity.isRunning) {
            stopSelf();
        }
    }

}