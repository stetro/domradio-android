package de.domradio.activity.util;

import android.graphics.PorterDuff;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import de.domradio.R;


public class ProgressClient extends WebChromeClient {
    private ProgressBar progressBar;

    public ProgressClient(ProgressBar progressBar) {
        int color = progressBar.getContext().getResources().getColor(R.color.app_color_dark);
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
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
