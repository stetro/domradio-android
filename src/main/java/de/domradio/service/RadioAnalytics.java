package de.domradio.service;


import android.app.Service;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Date;

import de.domradio.DomradioApplication;

public class RadioAnalytics {
    private final Service service;

    private Date started;

    public RadioAnalytics(Service service) {
        this.service = service;
    }

    public void sendRadioStartedAnalyticsEvent() {
        if (started == null) {
            Tracker appTracker = ((DomradioApplication) service.getApplication()).getAppTracker();
            appTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("DOMRADIO_APP")
                            .setAction("PLAYER_STARTED")
                            .setLabel("Player has started ")
                            .setValue(1L)
                            .build()
            );
        }
        started = new Date();
    }

    public void sendRadioStoppedAnalyticsEvent() {
        if (started != null) {
            Tracker appTracker = ((DomradioApplication) service.getApplication()).getAppTracker();
            long seconds = (new Date().getTime() - started.getTime()) / 1000;
            appTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("DOMRADIO_APP")
                            .setAction("PLAYER_STOPPED")
                            .setLabel("Player has stopped after " + seconds + " seconds of playing")
                            .setValue(seconds)
                            .build()
            );
        }
        started = null;
    }
}
