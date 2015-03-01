package de.stetro.domradio.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.greenrobot.event.EventBus;
import de.stetro.domradio.service.event.PauseRadioEvent;
import de.stetro.domradio.service.event.StartRadioEvent;
import de.stetro.domradio.service.event.StopRadioEvent;

public class RadioService extends Service implements OnCompletionListener,
        OnPreparedListener, OnErrorListener {

    public URL m_url = null;
    public MediaPlayer m_media_player = null;
    public String m_errors = "";
    public static State state = State.STOPPED;

    enum State {
        STOPPED, STARTING, PLAYING, PAUSED
    }

    public static State get_state() {
        return state;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        state = State.STOPPED;
        Log.d("Complete", "I am now Complete!");
        mp.release();
        if (mp.equals(m_media_player)) {
            m_media_player = null;
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("Start", "I am now prepared, lets start!");
        mp.start();
        state = State.PLAYING;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("Complete",
                "OMG ERROR: " + String.valueOf(what) + " - " + String.valueOf(extra));
        m_errors = "Error with connection to stream";
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
    }

    public RadioService() {
        state = State.STOPPED;
    }

    public void onEvent(StopRadioEvent event) {
        if (m_media_player != null) {
            Log.d("stop", "I existed so I'm cleaning up");
            m_media_player.reset();
            Log.d("stop", "Alright, im cleared up");
            m_media_player.release();
            Log.d("stop", "And now im shut down");
            m_media_player = null;

        }
        EventBus.getDefault().post(new StopSuccessRadioEvent());
        state = State.STOPPED;
    }

    public void onEvent(PauseRadioEvent event) {
        if (m_media_player != null) {
            if (state == State.PLAYING) {
                m_media_player.pause();
            }
        }
        state = State.PAUSED;
    }

    public void onEvent(StartRadioEvent event) {
        m_errors = "";
        try {
            m_url = new URL(event.getUrl());
        } catch (MalformedURLException e) {
            m_errors += "Error parsing URL (" + event.getUrl() + "): " + e.toString() + "\n";
            Log.e("download", m_errors);
        }

        if (m_errors.equals("")) {
            Log.d("start", "OK I hear you, lets start!");
            state = State.STARTING;
            try {
                if (m_media_player == null) {
                    Log.d("start", "I didn't exist yet so let me get prepared.");
                    m_media_player = new MediaPlayer();
                    m_media_player.setDataSource(event.getUrl());
                    m_media_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    m_media_player.setOnPreparedListener(this);
                    m_media_player.setOnCompletionListener(this);
                    m_media_player.setOnErrorListener(this);
                    m_media_player.prepareAsync();
                } else if (!m_media_player.isPlaying()) {
                    Log.d("start", "I exist but i'm not playing? let me fix that...");
                    if (state != State.STOPPED) {
                        Log.d("start", "Killing current connection...");
                        m_media_player.reset();
                        m_media_player.release();
                        m_media_player = null;
                        onEvent(event);
                    } else {
                        Log.d("start", "Hitting play");
                        m_media_player.start();
                        if (m_media_player.isPlaying()) {
                            state = State.PLAYING;
                        } else {
                            m_errors += "Error starting stream: object created but wont restart\n";
                        }
                    }
                }
            } catch (IOException e) {
                m_errors += "Error starting stream: " + e.toString() + "\n";
                Log.e("start", m_errors, e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}