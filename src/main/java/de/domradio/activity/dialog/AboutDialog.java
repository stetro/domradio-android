package de.domradio.activity.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;

import de.domradio.R;
import de.domradio.service.AnalyticsTracker;


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
        AnalyticsTracker.openAbout(context.getApplication());
    }


    @Override
    public void onNeutral(MaterialDialog dialog) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(context.getString(R.string.domradioweb)));
        context.startActivity(i);
        AnalyticsTracker.openDomradio(context.getApplication());
        super.onNeutral(dialog);
    }
}
