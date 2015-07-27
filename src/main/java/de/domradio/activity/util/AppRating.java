package de.domradio.activity.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import de.domradio.service.AnalyticsTracker;


public class AppRating {
    public static void rateThisApp(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Uri playStoreUri = Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName());
            activity.startActivity(new Intent(Intent.ACTION_VIEW, playStoreUri));
        }
        AnalyticsTracker.openRating(activity.getApplication());
    }
}
