package de.domradio.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.domradio.R;
import de.domradio.activity.dialog.AboutDialog;
import de.domradio.service.AnalyticsTracker;
import de.domradio.service.RadioService;
import de.domradio.service.RadioServiceState;

public class MainActivity extends BaseActivity {

    public volatile static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);
        startRadioService();
        isRunning = true;
    }

    private void startRadioService() {
        Intent intent = new Intent(getApplicationContext(), RadioService.class);
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
        switch (item.getItemId()) {
            case R.id.main_activity_menu_about:
                new AboutDialog(this).show();
                return true;
            case R.id.main_activity_menu_rate:
                rateThisApp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void rateThisApp() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
        AnalyticsTracker.openRating(getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (RadioService.get_state().equals(RadioServiceState.STOPPED)) {
            stopService(new Intent(this, RadioService.class));
        }
    }
}
