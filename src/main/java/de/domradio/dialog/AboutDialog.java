package de.domradio.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import de.domradio.R;


public class AboutDialog extends MaterialDialog.ButtonCallback implements Dialog {
    private final MaterialDialog dialog;

    public AboutDialog(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .callback(this)
                .title(R.string.about)
                .positiveText(R.string.ok)
                .content(R.string.about_text)
                .build();
    }

    @Override
    public void show() {
        dialog.show();
    }
}
