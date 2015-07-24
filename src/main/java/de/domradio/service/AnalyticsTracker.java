package de.domradio.service;


import android.app.Application;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.domradio.DomradioApplication;
import de.domradio.domain.News;

public class AnalyticsTracker {
    public static void openRating(Application application) {
        Tracker appTracker = ((DomradioApplication) application).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("RATING_OPENED")
                        .setLabel("Rating page was opened")
                        .setValue(0L)
                        .build()
        );
    }

    public static void shareArticle(Application application, String title) {
        Tracker appTracker = ((DomradioApplication) application).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("NEWS_SHARED")
                        .setLabel(title)
                        .setValue(0L)
                        .build()
        );
    }

    public static void openAbout(Application application) {
        Tracker appTracker = ((DomradioApplication) application).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("ABOUT_OPENED")
                        .setLabel("About page was opened")
                        .setValue(0L)
                        .build()
        );
    }

    public static void openDomradio(Application application) {
        Tracker appTracker = ((DomradioApplication) application).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("DOMRADIO_WEB_OPENED")
                        .setLabel("domradio.de was opened")
                        .setValue(0L)
                        .build()
        );
    }

    public static void openNews(Application application, News n) {
        Tracker appTracker = ((DomradioApplication) application).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("NEWS_OPENED")
                        .setLabel(n.getTitle())
                        .setValue(0L)
                        .build()
        );
    }
}
