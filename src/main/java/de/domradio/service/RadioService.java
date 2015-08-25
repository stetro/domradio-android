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

import java.io.IOException;

import de.domradio.R;
import de.domradio.activity.MainActivity;
import de.domradio.service.event.ErrorEvent;
import de.domradio.service.event.RadioStartedEvent;
import de.domradio.service.event.RadioStartingEvent;
import de.domradio.service.event.RadioStoppedEvent;
import de.domradio.service.event.StartRadioEvent;
import de.domradio.service.event.StopRadioEvent;
import de.greenrobot.event.EventBus;

public class RadioService extends Service implements OnCompletionListener, OnPreparedListener, OnErrorListener {

    public static final String RADIO_URL_LOW = "http://domradio-mp3-l.akacast.akamaistream.net/7/809/237368/v1/gnl.akacast.akamaistream.net/domradio-mp3-l";
    public volatile static RadioServiceState radioServiceState = RadioServiceState.STOPPED;
    public MediaPlayer mediaPlayer = null;
    private WifiManager.WifiLock wifiLock;
    private RadioAnalyticsTracker radioAnalyticsTracker;

    public RadioService() {
        radioServiceState = RadioServiceState.STOPPED;
        EventBus.getDefault().post(new RadioStoppedEvent());
    }

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
        EventBus.getDefault().post(new ErrorEvent(getString(R.string.error_mediaplayer)));
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        radioAnalyticsTracker = new RadioAnalyticsTracker(this);
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

    @EventBusCallback
    public void onEvent(StartRadioEvent event) {
        String errorMessage = "";
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

    @EventBusCallback
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

    @EventBusCallback
    public void onEvent(RadioStartingEvent e) {
        acquireWifiLock();
    }

    @EventBusCallback
    public void onEvent(RadioStartedEvent e) {
        acquireWifiLock();
        radioAnalyticsTracker.sendRadioStartedAnalyticsEvent();
        startForeground(RadioNotification.DEFAULT_NOTIFICATION_ID, RadioNotification.getStickyNotification(this));
    }

    @EventBusCallback
    public void onEvent(RadioStoppedEvent e) {
        releaseWifiLock();
        stopForeground(true);
        radioAnalyticsTracker.sendRadioStoppedAnalyticsEvent();
        if (!MainActivity.isActive) {
            stopSelf();
        }
    }

}