package de.stetro.domradio.service.event;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
