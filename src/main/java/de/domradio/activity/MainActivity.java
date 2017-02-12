package de.domradio.activity;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import java.util.ArrayList;
import java.util.List;

import de.domradio.R;
import de.domradio.activity.adapter.AppBarViewAdapter;
import de.domradio.activity.adapter.PlayerViewAdapter;
import de.domradio.activity.adapter.ViewAdapter;
import de.domradio.activity.dialog.AboutDialog;
import de.domradio.activity.util.AppRating;
import de.domradio.service.EventBusCallback;
import de.domradio.service.event.ErrorEvent;
import de.domradio.service.event.StartLoadingFeedEvent;
import de.domradio.service.event.StopLoadingFeedEvent;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    public volatile static boolean isActive = false;
    private List<ViewAdapter> viewAdapterList = new ArrayList<>();
    private ProgressBar loaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);
        loaderView = (ProgressBar) findViewById(R.id.loader);
        registerViewAdapter();
        isActive = true;
    }

    private void registerViewAdapter() {
        viewAdapterList.clear();
        viewAdapterList.add(new PlayerViewAdapter());
        viewAdapterList.add(new AppBarViewAdapter());
        for (ViewAdapter adapter : viewAdapterList) {
            adapter.register(this);
        }
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
                AppRating.rateThisApp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
        unregisterViewAdapters();
        EventBus.getDefault().unregister(this);
    }

    private void unregisterViewAdapters() {
        for (ViewAdapter adapter : viewAdapterList) {
            adapter.unregister(this);
        }
    }

    @EventBusCallback
    public void onEvent(ErrorEvent e) {
        View rootView = findViewById(R.id.root_view);
        if (rootView != null) {
            Snackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @EventBusCallback
    public void onEvent(StartLoadingFeedEvent event) {
        loaderView.setVisibility(View.VISIBLE);
    }

    @EventBusCallback
    public void onEvent(StopLoadingFeedEvent event) {
        loaderView.setVisibility(View.GONE);
    }
}
