package de.domradio.activity.util;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gc.materialdesign.views.ProgressBarDeterminate;


public class ProgressClient extends WebChromeClient {
    private ProgressBarDeterminate progressBar;

    public ProgressClient(ProgressBarDeterminate progressBar) {
        this.progressBar = progressBar;
    }

    public void onProgressChanged(WebView view, int progress) {
        if (progress < 100 && progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (progressBar.isEnabled()) {
            progressBar.setProgress(progress);
        }
        if (progress == 100) {
            progressBar.setVisibility(View.GONE);
        }
    }

}
