package de.domradio;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


public class DomradioApplication extends Application {

    public Tracker appTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        createTracker();
    }

    private void createTracker() {
        if (appTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            appTracker = analytics.newTracker(R.xml.global_tracker);
            appTracker.enableAdvertisingIdCollection(true);
        }
    }

    public Tracker getAppTracker() {
        createTracker();
        return appTracker;
    }
}
