package de.domradio.activity.adapter;

import android.app.Activity;
import android.util.Log;

import de.domradio.service.EventBusCallback;
import de.domradio.service.event.SetNewsFeedEvent;
import de.greenrobot.event.EventBus;


public class AppBarViewAdapter implements ViewAdapter {

    @Override
    public void register(Activity activity) {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregister(Activity activity) {
        EventBus.getDefault().unregister(this);
    }

    @EventBusCallback
    public void onEvent(SetNewsFeedEvent e) {
        Log.d("AppBarViewAdapter", e.getFeedTopic().getTitle());
    }
}
