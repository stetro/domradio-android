package de.domradio.activity.dialog;


import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import de.domradio.R;
import de.domradio.domain.FeedTopic;
import de.domradio.service.event.SetNewsFeedEvent;
import de.greenrobot.event.EventBus;

public class RssChooserDialog implements Dialog, MaterialDialog.ListCallback {
    private final MaterialDialog dialog;

    public RssChooserDialog(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.rss_feed_topic)
                .items(FeedTopic.getTitles())
                .itemsCallback(this)
                .negativeText(R.string.cancel)
                .build();
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
        this.dialog.hide();
        FeedTopic feedTopic = FeedTopic.resolve(charSequence);
        if (feedTopic != null) {
            EventBus.getDefault().post(new SetNewsFeedEvent(feedTopic));
        }
    }
}
