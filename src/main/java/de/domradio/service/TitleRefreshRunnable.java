package de.domradio.service;

import android.util.Log;

import de.domradio.domain.Station;
import de.domradio.service.event.UpdatePlayerTitleEvent;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

public class TitleRefreshRunnable implements Runnable {

    private boolean running = true;
    private DomradioRadioSerive service;

    @Override
    public void run() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.domradio.de")
                .setConverter(new SimpleXMLConverter())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        service = restAdapter.create(DomradioRadioSerive.class);

        try {
            while (isRunning()) {
                requestCurrentPlayerTitle();
                Thread.sleep(20000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private void requestCurrentPlayerTitle() {
        service.getCurrentPlayerTitle(new Callback<Station>() {

            @Override
            public void success(Station station, Response response) {
                EventBus.getDefault().post(new UpdatePlayerTitleEvent(station));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Title Refreshment", "Could not Load Infos - " + error.getMessage());
            }
        });
    }

    private interface DomradioRadioSerive {
        @GET("/sites/all/themes/domradio/playlist/Export.xml")
        void getCurrentPlayerTitle(Callback<Station> callback);
    }
}
