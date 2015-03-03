package de.domradio.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import de.greenrobot.event.EventBus;
import de.domradio.R;
import de.domradio.service.event.StartRadioEvent;


public class ConfirmNoWifiDialog extends MaterialDialog.ButtonCallback implements Dialog {
    private final MaterialDialog dialog;

    public ConfirmNoWifiDialog(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.no_wifi_title)
                .content(R.string.no_wifi_description)
                .callback(this)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .build();
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        super.onPositive(dialog);
        EventBus.getDefault().post(new StartRadioEvent());
    }

}
