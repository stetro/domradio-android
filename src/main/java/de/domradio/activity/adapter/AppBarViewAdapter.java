package de.domradio.activity.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.domradio.R;
import de.domradio.activity.MainActivity;
import de.domradio.activity.dialog.RssChooserDialog;
import de.domradio.domain.FeedTopic;
import de.domradio.service.EventBusCallback;
import de.domradio.service.event.SetNewsFeedEvent;
import de.greenrobot.event.EventBus;


public class AppBarViewAdapter implements ViewAdapter {

    private TextView feedTitle;

    @Override
    public void register(final MainActivity activity) {
        feedTitle = (TextView) activity.findViewById(R.id.feed_title);
        feedTitle.setText(FeedTopic.ALL.getTitle());
        feedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RssChooserDialog(activity).show();
            }
        });
        ImageButton feedTitleButton = (ImageButton) activity.findViewById(R.id.feed_title_button);
        feedTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RssChooserDialog(activity).show();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregister(MainActivity activity) {
        EventBus.getDefault().unregister(this);
    }

    @EventBusCallback
    public void onEvent(SetNewsFeedEvent e) {
        feedTitle.setText(e.getFeedTopic().getTitle());
    }
}
