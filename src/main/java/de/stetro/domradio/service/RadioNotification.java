package de.stetro.domradio.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import de.greenrobot.event.EventBus;
import de.stetro.domradio.R;
import de.stetro.domradio.activity.MainActivity;
import de.stetro.domradio.service.event.StopRadioEvent;

public class RadioNotification extends BroadcastReceiver {

    public static final int DEFAULT_NOTIFICATION_ID = 0x3333;
    public static final String TOGGLE_ACTION = "de.stetro.domradio.ACTION_TOGGLE";

    public static Notification getStickyNotification(Context context) {
        NotificationCompat.Builder mBuilder = getNotificationBuilder(context);
        mBuilder.addAction(R.drawable.ic_pause, "Stoppen", getToggleRadioPendingIntent(context));
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    private static NotificationCompat.Builder getNotificationBuilder(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_nf_play);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setContentText(context.getString(R.string.radio_live_stream));

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_radio);
        mBuilder.setLargeIcon(bitmap);
        mBuilder.setContentIntent(getMainActivityPendingIntent(context));
        return mBuilder;
    }

    private static PendingIntent getToggleRadioPendingIntent(Context context) {
        Intent resultIntent = new Intent(TOGGLE_ACTION);
        return PendingIntent.getBroadcast(context, 100, resultIntent, 0);
    }

    private static PendingIntent getMainActivityPendingIntent(Context context) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RadioNotification", "onReceive " + intent.getAction());
        if (intent.getAction().equalsIgnoreCase(TOGGLE_ACTION)) {
            switch (RadioService.get_state()) {
                default:
                    EventBus.getDefault().post(new StopRadioEvent());
                    break;
            }
        }
    }
}
