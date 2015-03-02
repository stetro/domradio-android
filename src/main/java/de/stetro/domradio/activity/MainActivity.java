package de.stetro.domradio.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.stetro.domradio.R;
import de.stetro.domradio.dialog.AboutDialog;
import de.stetro.domradio.service.RadioNotification;
import de.stetro.domradio.service.RadioService;
import de.stetro.domradio.service.event.StopAppEvent;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);
        startRadioService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RadioNotification.removeStickyNotification(this);
    }

    private void startRadioService() {
        Intent intent = new Intent(this, RadioService.class);
        this.startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_acticity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_activity_menu_about:
                new AboutDialog(this).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        RadioNotification.addStickyNotification(this);
        super.onPause();
    }

    public void onEvent(StopAppEvent e){
        this.finish();
    }
}
