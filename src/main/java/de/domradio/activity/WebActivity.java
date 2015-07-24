package de.domradio.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;

import de.domradio.R;
import de.domradio.activity.util.ProgressClient;
import de.domradio.service.AnalyticsTracker;

public class WebActivity extends BaseActivity {
    private WebView webView;
    private String title;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        title = getIntent().getStringExtra("title");
        link = getIntent().getStringExtra("link");
        fillContent();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void fillContent() {
        setTitle(title);
        webView = (WebView) findViewById(R.id.webview);
        View progressBar = findViewById(R.id.web_progress);
        WebChromeClient client = new ProgressClient((ProgressBarIndeterminateDeterminate) progressBar);
        webView.setWebChromeClient(client);
        webView.loadUrl(link);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_acticity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, title);
                i.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(i, getString(R.string.share_article)));
                AnalyticsTracker.shareArticle(getApplication(), title);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
