package de.domradio.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.domradio.DomradioApplication;
import de.domradio.R;


public class AboutDialog extends MaterialDialog.ButtonCallback implements Dialog {
    private final MaterialDialog dialog;
    private Activity context;

    public AboutDialog(Activity context) {
        this.context = context;
        dialog = new MaterialDialog.Builder(context)
                .callback(this)
                .title(R.string.about)
                .positiveText(R.string.ok)
                .neutralText(R.string.domradiode)
                .content(R.string.about_text)
                .callback(this)
                .build();
    }

    @Override
    public void show() {
        dialog.show();
        sendAboutAnalyticsEvent();
    }

    private void sendAboutAnalyticsEvent() {
        Tracker appTracker = ((DomradioApplication) context.getApplication()).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("ABOUT_OPENED")
                        .setLabel("About page was opened")
                        .setValue(0L)
                        .build()
        );
    }

    private void sendOpenWebAnalyticsEvent() {
        Tracker appTracker = ((DomradioApplication) context.getApplication()).getAppTracker();
        appTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DOMRADIO_APP")
                        .setAction("DOMRADIO_WEB_OPENED")
                        .setLabel("domradio.de was opened")
                        .setValue(0L)
                        .build()
        );
    }

    @Override
    public void onNeutral(MaterialDialog dialog) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(context.getString(R.string.domradioweb)));
        context.startActivity(i);
        sendOpenWebAnalyticsEvent();
        super.onNeutral(dialog);
    }
}
